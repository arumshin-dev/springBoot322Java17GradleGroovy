package com.example.springBoot322Java17GradleGroovy.router.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginRouter {

    @GetMapping("/login")
    public String login() {return "login";}
}