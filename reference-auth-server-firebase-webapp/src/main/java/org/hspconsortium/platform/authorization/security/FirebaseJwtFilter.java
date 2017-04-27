package org.hspconsortium.platform.authorization.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FirebaseJwtFilter extends GenericFilterBean {

    public static final String COOKIE_NAME = "hspc-token";
    private final Logger log = LoggerFactory.getLogger(FirebaseJwtFilter.class);


    public FirebaseJwtFilter() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        addAuthIfCookieExists((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void addAuthIfCookieExists(HttpServletRequest httpServletRequest) {
        String jwt = resolveToken(httpServletRequest);
        if (jwt != null) {
            FirebaseJwtAuthenticationToken hspcAuthToken = new FirebaseJwtAuthenticationToken(jwt);
            SecurityContextHolder.getContext().setAuthentication(hspcAuthToken);
        }
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
