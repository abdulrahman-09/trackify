package com.mujahid.trackify.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mujahid.trackify.security.Principal;
import com.mujahid.trackify.security.dto.response.AuthenticationResponse;
import com.mujahid.trackify.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Principal principal = (Principal) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        AuthenticationResponse authResponse =
                new AuthenticationResponse(token, "Bearer");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getWriter(), authResponse);
    }
}
