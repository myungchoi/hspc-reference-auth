package org.hspconsortium.platform.authentication.persona;

import org.hspconsortium.platform.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

/**
 * Created by mike on 4/12/17.
 */
public class PersonaAuthInterceptor extends HandlerInterceptorAdapter {

    public static final String HSPC_PERSONA_TOKEN_NAME = "hspc-persona-token";

    @Inject
    private JwtService jwtService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (httpServletRequest.getServletPath().startsWith("/authorize")) {
            authenticatePersonaUser(httpServletRequest);
        } else if (httpServletRequest.getServletPath().startsWith("/token")) {
            removePersonaCookie(httpServletRequest, httpServletResponse);
        }

        return true;
    }

    private void removePersonaCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        for (Cookie cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equals(HSPC_PERSONA_TOKEN_NAME)) {
                cookie.setDomain("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                httpServletResponse.addCookie(cookie);
            }
        }
    }

    private void authenticatePersonaUser(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        Cookie hspcPersonaTokenCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(HSPC_PERSONA_TOKEN_NAME)) {
                hspcPersonaTokenCookie = cookie;
            }
        }

        // if there is no persona cookie, proceed as normal
        if (hspcPersonaTokenCookie == null) {
            return;
        }

        UsernamePasswordAuthenticationToken personaAuthentication = generatePersonaAuthentication(hspcPersonaTokenCookie.getValue());

        SecurityContext personaSecurityContext = SecurityContextHolder.createEmptyContext();
        personaSecurityContext.setAuthentication(personaAuthentication);
        SecurityContextHolder.setContext(personaSecurityContext);
    }

    private UsernamePasswordAuthenticationToken generatePersonaAuthentication(String personaJwtString) {

        String username = jwtService.usernameFromJwt(personaJwtString);

        if (username == null) {
            throw new SecurityException("Invalid JWT while trying to authenticate persona user.");
        }

        List<SimpleGrantedAuthority> personaAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        User personaUser = new User(username, "password", personaAuthorities);
        return new UsernamePasswordAuthenticationToken(personaUser, null, personaAuthorities);
    }
}
