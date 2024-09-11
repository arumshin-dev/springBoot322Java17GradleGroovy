package com.example.springBoot322Java17GradleGroovy.router.index;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexRouter {

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("pageTitle","Index Page");
        return "index";
    }
}
