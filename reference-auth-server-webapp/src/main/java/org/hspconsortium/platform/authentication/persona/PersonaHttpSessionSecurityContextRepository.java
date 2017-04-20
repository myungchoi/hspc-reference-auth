package org.hspconsortium.platform.authentication.persona;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class will disable security from interacting with the session when a persona user is being
 * authenticated.  This allows the persona user to go through it's flow, without logging out the
 * current real user.
 */
public class PersonaHttpSessionSecurityContextRepository extends HttpSessionSecurityContextRepository {

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        if (shouldInteractWithSession(request)) {
            super.saveContext(context, request, response);
        }
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        if (shouldInteractWithSession(requestResponseHolder.getRequest())) {
            return super.loadContext(requestResponseHolder);
        }
        return SecurityContextHolder.createEmptyContext();
    }

    private boolean shouldInteractWithSession(HttpServletRequest request) {
        if (!request.getServletPath().startsWith("/authorize")) {
            return true;
        }

        String personaJwtString = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(PersonaAuthInterceptor.HSPC_PERSONA_TOKEN_NAME)) {
                personaJwtString = cookie.getValue();
            }
        }

        return personaJwtString == null;
    }
}
