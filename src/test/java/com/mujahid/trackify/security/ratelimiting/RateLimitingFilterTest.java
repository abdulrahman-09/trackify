package com.mujahid.trackify.security.ratelimiting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimitingFilterTest {

    private RateLimitingFilter filter;

    @BeforeEach
    void setUp() throws Exception {
        filter = new RateLimitingFilter();

        // Inject trustedProxies since @Value doesn't work outside Spring context
        Field trustedProxiesField = RateLimitingFilter.class.getDeclaredField("trustedProxies");
        trustedProxiesField.setAccessible(true);
        trustedProxiesField.set(filter, Set.of("127.0.0.1"));
    }

    // ─── Helper methods ───────────────────────────────────────────────────────

    private MockHttpServletRequest buildRequest(String path, String remoteAddr) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI(path);
        request.setRemoteAddr(remoteAddr);
        request.setContentType("application/json");
        return request;
    }

    private void fireRequests(int count, String path, String ip) throws Exception {
        for (int i = 0; i < count; i++) {
            MockHttpServletRequest request = buildRequest(path, ip);
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilterInternal(request, response, new MockFilterChain());
        }
    }

    // ─── Core rate limiting ───────────────────────────────────────────────────

    @Test
    void shouldAllowRequestsUnderTheLimit() throws Exception {
        // 5 requests should all pass through
        for (int i = 0; i < 5; i++) {
            MockHttpServletRequest request = buildRequest("/api/v1/auth/login", "10.0.0.1");
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain chain = new MockFilterChain();

            filter.doFilterInternal(request, response, chain);

            // 0 means filter didn't write a response — it passed to the chain
            assertThat(response.getStatus())
                    .as("Request %d should pass through", i + 1)
                    .isEqualTo(200);
        }
    }

    @Test
    void shouldReturn429AfterExceedingLimit() throws Exception {
        String ip = "10.0.0.2";

        // Exhaust the 5 allowed requests
        fireRequests(5, "/api/v1/auth/login", ip);

        // 6th request should be blocked
        MockHttpServletRequest request = buildRequest("/api/v1/auth/login", ip);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(429);
    }

    @Test
    void shouldIncludeRetryAfterHeaderOnRateLimitResponse() throws Exception {
        String ip = "10.0.0.3";

        fireRequests(5, "/api/v1/auth/login", ip);

        MockHttpServletRequest request = buildRequest("/api/v1/auth/login", ip);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getHeader("Retry-After")).isEqualTo("60");
    }

    @Test
    void shouldIncludeCorrectContentTypeOnRateLimitResponse() throws Exception {
        String ip = "10.0.0.4";

        fireRequests(5, "/api/v1/auth/login", ip);

        MockHttpServletRequest request = buildRequest("/api/v1/auth/login", ip);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getContentType()).contains("application/json");
    }

    @Test
    void shouldIncludeErrorMessageInBodyOnRateLimitResponse() throws Exception {
        String ip = "10.0.0.5";

        fireRequests(5, "/api/v1/auth/login", ip);

        MockHttpServletRequest request = buildRequest("/api/v1/auth/login", ip);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getContentAsString()).contains("Too many requests");
    }

    // ─── Endpoint coverage ────────────────────────────────────────────────────

    @Test
    void shouldRateLimitRegisterEndpointToo() throws Exception {
        String ip = "10.0.0.6";

        fireRequests(5, "/api/v1/auth/register", ip);

        MockHttpServletRequest request = buildRequest("/api/v1/auth/register", ip);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(429);
    }

    @Test
    void shouldNotRateLimitNonAuthEndpoints() throws Exception {
        String ip = "10.0.0.7";
        String path = "/actuator/health";

        // Fire well over the limit
        for (int i = 0; i < 10; i++) {
            MockHttpServletRequest request = buildRequest(path, ip);
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain chain = new MockFilterChain();

            filter.doFilterInternal(request, response, chain);

            // Should never be rate limited
            assertThat(response.getStatus())
                    .as("Non-auth request %d should never be rate limited", i + 1)
                    .isNotEqualTo(429);
        }
    }

    // ─── IP isolation ─────────────────────────────────────────────────────────

    @Test
    void shouldTrackRateLimitPerIpIndependently() throws Exception {
        // IP A exhausts its limit
        fireRequests(5, "/api/v1/auth/login", "10.1.1.1");

        // IP B should still be allowed — has its own fresh bucket
        MockHttpServletRequest request = buildRequest("/api/v1/auth/login", "10.1.1.2");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isNotEqualTo(429);
    }

    // ─── X-Forwarded-For handling ─────────────────────────────────────────────

    @Test
    void shouldUseXForwardedForWhenRequestComesFromTrustedProxy() throws Exception {
        String realClientIp = "203.0.113.42";

        // Exhaust limit for the real client IP coming through trusted proxy
        for (int i = 0; i < 5; i++) {
            MockHttpServletRequest request = buildRequest("/api/v1/auth/login", "127.0.0.1");
            request.addHeader("X-Forwarded-For", realClientIp);
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilterInternal(request, response, new MockFilterChain());
        }

        // 6th request from same real IP through proxy should be blocked
        MockHttpServletRequest request = buildRequest("/api/v1/auth/login", "127.0.0.1");
        request.addHeader("X-Forwarded-For", realClientIp);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(429);
    }

    @Test
    void shouldIgnoreXForwardedForWhenRequestComesFromUntrustedSource() throws Exception {
        String attackerRealIp = "85.23.44.12";

        // Attacker sends 5 requests with different fake IPs in X-Forwarded-For
        for (int i = 1; i <= 5; i++) {
            MockHttpServletRequest request = buildRequest("/api/v1/auth/login", attackerRealIp);
            request.addHeader("X-Forwarded-For", "1.2.3." + i); // different fake IP each time
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilterInternal(request, response, new MockFilterChain());
        }

        // 6th request — spoofed header ignored, real IP is tracked, should be blocked
        MockHttpServletRequest request = buildRequest("/api/v1/auth/login", attackerRealIp);
        request.addHeader("X-Forwarded-For", "9.9.9.9");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(429);
    }

    @Test
    void shouldUseFirstIpFromMultipleXForwardedForValues() throws Exception {
        // X-Forwarded-For can have multiple IPs when there are multiple proxies:
        // X-Forwarded-For: <real-client>, <proxy1>, <proxy2>
        // We always want the first one (real client)
        String realClientIp = "203.0.113.99";

        for (int i = 0; i < 5; i++) {
            MockHttpServletRequest request = buildRequest("/api/v1/auth/login", "127.0.0.1");
            // Simulate two proxies in the chain
            request.addHeader("X-Forwarded-For", realClientIp + ", 10.0.0.1, 10.0.0.2");
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilterInternal(request, response, new MockFilterChain());
        }

        MockHttpServletRequest request = buildRequest("/api/v1/auth/login", "127.0.0.1");
        request.addHeader("X-Forwarded-For", realClientIp + ", 10.0.0.1, 10.0.0.2");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(429);
    }
}