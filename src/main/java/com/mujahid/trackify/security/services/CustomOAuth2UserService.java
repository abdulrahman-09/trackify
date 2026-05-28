package com.mujahid.trackify.security.services;

import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.repositories.UserRepository;
import com.mujahid.trackify.security.Principal;
import com.mujahid.trackify.security.enums.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String googleId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not returned by provider");
        }

        User user = userRepository.findByEmail(email)
                .map(existingUser -> updateExistingOAuth2User(existingUser, googleId))
                .orElseGet(() -> createNewOAuth2User(email, googleId, firstName, lastName));

        return new Principal(user, attributes);
    }

    private User updateExistingOAuth2User(User existingUser, String googleId) {
        if (existingUser.getProvider() == AuthProvider.LOCAL){
            existingUser.setProvider(AuthProvider.GOOGLE);
            existingUser.setAuthProviderId(googleId);
            userRepository.save(existingUser);
        }
        return existingUser;
    }

    private User createNewOAuth2User(String email, String googleId, String firstName, String lastName) {
        User user = User.builder()
                .email(email)
                .firstName(firstName != null ? firstName : "")
                .lastName(lastName != null ? lastName : "")
                .provider(AuthProvider.GOOGLE)
                .authProviderId(googleId)
                .build();

        return userRepository.save(user);
    }
}
