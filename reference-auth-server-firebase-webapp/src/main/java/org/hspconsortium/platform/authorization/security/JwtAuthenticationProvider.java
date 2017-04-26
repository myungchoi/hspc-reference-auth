package org.hspconsortium.platform.authorization.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by mike on 4/9/17.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    public boolean supports(Class<?> authenticaiton) {
        return authenticaiton.equals(
                UsernamePasswordAuthenticationToken.class
        );
    }
}
