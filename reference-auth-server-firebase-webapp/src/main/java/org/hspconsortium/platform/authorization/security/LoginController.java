package org.hspconsortium.platform.authorization.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @Value("${hspc.platform.accountLoginPage}")
    private String loginUrl;

    @Value("${hspc.platform.accountLogoutPage}")
    private String logoutUrl;

    @RequestMapping({"login", "login/"})
    public String doLoginRedirect() {
        return "redirect:" + loginUrl;
    }

    @RequestMapping({"logout", "logout/"})
    public String doLogoutRedirect() {
        return "redirect:" + logoutUrl;
    }
}
