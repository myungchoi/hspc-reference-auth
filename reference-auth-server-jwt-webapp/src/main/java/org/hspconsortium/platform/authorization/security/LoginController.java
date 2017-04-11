package org.hspconsortium.platform.authorization.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @Value("${oidc.authentication.url}")
    private String loginUrl;

    @RequestMapping({"login", "login/"})
    public String doLoginRedirect() {
        return "redirect:" + loginUrl;
    }
}
