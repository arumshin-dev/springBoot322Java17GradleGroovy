package com.example.springBoot322Java17GradleGroovy.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {

    @GetMapping("/hi")
    public String niceToMeetYou(Model model){
        model.addAttribute("userName","arum");
        return "greetings";//templates/greetings.mistache
    }

    @GetMapping("/bye")
    public String seeYouNext(Model model){
        model.addAttribute("nickName","arumi");
        return "goodbye";//templates/greetings.mistache
    }
}
