package org.hspconsortium.platform.authorization.security;

import com.google.firebase.auth.FirebaseToken;
import org.hspconsortium.platform.service.FirebaseTokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

public class FirebaseJwtAuthenticationProvider implements AuthenticationProvider {

    @Inject
    private FirebaseTokenService firebaseTokenService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FirebaseJwtAuthenticationToken firebaseJwtAuthenticationToken = (FirebaseJwtAuthenticationToken) authentication;
        FirebaseToken firebaseToken = firebaseTokenService.validateToken(firebaseJwtAuthenticationToken.getJwt());

        if (firebaseToken == null) {
            // the token is invalid
            return null;
        }

        FirebaseJwtAuthenticationToken toReturn = new FirebaseJwtAuthenticationToken(firebaseToken, retrieveAuthorities());
        toReturn.setAuthenticated(true);
        return toReturn;
    }

    public boolean supports(Class<?> authenticaiton) {
        return authenticaiton.equals(FirebaseJwtAuthenticationToken.class);
    }

    private Collection<GrantedAuthority> retrieveAuthorities() {
        ArrayList<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        return auths;
    }
}