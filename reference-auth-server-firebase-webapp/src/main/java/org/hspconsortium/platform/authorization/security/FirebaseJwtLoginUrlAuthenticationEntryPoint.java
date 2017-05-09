package org.hspconsortium.platform.authorization.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FirebaseJwtLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    public FirebaseJwtLoginUrlAuthenticationEntryPoint(String loginFormUrl, String baseUrl) {
        super(loginFormUrl);
        this.baseUrl = baseUrl;
    }

    private String baseUrl;

    @Override
    protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        StringBuilder loginFormBuilder = new StringBuilder(determineUrlToUseForThisRequest(request, response, authException));
        try {
            loginFormBuilder.append("?afterAuth=");
            loginFormBuilder.append(URLEncoder.encode(this.baseUrl, "UTF-8"));
            loginFormBuilder.append(request.getServletPath().equals("/") ? "" : URLEncoder.encode(request.getServletPath(), "UTF-8"));
            if (request.getQueryString() != null) {
                loginFormBuilder.append(URLEncoder.encode("?" + request.getQueryString(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return loginFormBuilder.toString();
    }
}