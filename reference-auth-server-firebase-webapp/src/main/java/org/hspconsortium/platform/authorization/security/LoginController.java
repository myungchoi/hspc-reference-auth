package org.hspconsortium.platform.authorization.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class LoginController {

    @Value("${hspc.platform.accountLoginPage}")
    private String loginUrl;

    @Value("${hspc.platform.accountLogoutPage}")
    private String logoutUrl;

    @Value("${oidc.issuer}")
    private String oidcIssuer;

    @RequestMapping({"login", "login/"})
    public String doLoginRedirect() {
        return "redirect:" + loginUrl + buildRedirectUrlParam("afterAuth");
    }

    @RequestMapping({"logout", "logout/"})
    public String doLogoutRedirect() {
        return "redirect:" + logoutUrl + buildRedirectUrlParam("afterLogout");
    }

    private String buildRedirectUrlParam(String paramName) {
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        builder.append(paramName);
        builder.append("=");
        try {
            builder.append(URLEncoder.encode(oidcIssuer, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return builder.toString();
    }
}