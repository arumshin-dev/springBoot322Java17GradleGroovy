package com.example.springBoot322Java17GradleGroovy.router.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginRouter {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle","Login Page");
        return "login";
    }

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("pageTitle","Join Page");
        return "join";
    }

    @GetMapping("/lgn")
    public String lgn(Model model) {
        model.addAttribute("pageTitle", "Login Page");
        return "lgn";//templates/lgn.mustache
    }
}
