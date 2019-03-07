package com.lovesickness.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/local")
public class LocalController {
    @GetMapping(value = "/accountbind")
    public String accountBind() {
        return "local/accountbind";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "local/login";
    }

    @GetMapping(value = "/changepwd")
    public String changePwd() {
        return "local/changepwd";
    }
}
