package com.mujahid.trackify.security.ratelimiting;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 5;
    private static final int WINDOW_MINUTES = 1;
    private static final int RETRY_AFTER_SECONDS = WINDOW_MINUTES * 60;

    // Trusted proxy IPs - externalize to config in production
    @Value("${rate-limit.trusted-proxies:127.0.0.1}")
    private Set<String> trustedProxies;

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .maximumSize(10_000)
            .build();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (isAuthEndpoint(path)) {
            String clientId = getClientIdentifier(request);
            Bucket bucket = resolveBucket(clientId);

            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                log.warn("Rate limit exceeded for client: {}", clientId);
                sendRateLimitResponse(response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isAuthEndpoint(String path) {
        return path.startsWith("/api/v1/auth/");
    }

    private Bucket resolveBucket(String clientId) {
        return cache.get(clientId, k -> createNewBucket());
    }

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(MAX_REQUESTS)
                        .refillIntervally(MAX_REQUESTS, Duration.ofMinutes(WINDOW_MINUTES)))
                .build();
    }

    private String getClientIdentifier(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();

        if (isTrustedProxy(remoteAddr)) {
            String forwarded = request.getHeader("X-Forwarded-For");
            if (forwarded != null && !forwarded.isBlank()) {
                return forwarded.split(",")[0].trim();
            }
        }

        return remoteAddr;
    }

    private boolean isTrustedProxy(String ip) {
        return trustedProxies.contains(ip);
    }

    private void sendRateLimitResponse(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Retry-After", String.valueOf(RETRY_AFTER_SECONDS));
        response.getWriter().write("{\"error\":\"Too many requests. Try again in a minute.\"}");
    }
}