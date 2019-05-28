package com.lovesickness.o2o.web.local;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/local")
@Api(tags = "LocalController|本地账户界面跳转控制器")
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

    @GetMapping(value = "/personoperation")
    public String personOperation() {
        return "local/personoperation";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "local/register";
    }
}
