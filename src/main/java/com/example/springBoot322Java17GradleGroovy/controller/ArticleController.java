package com.example.springBoot322Java17GradleGroovy.controller;

import com.example.springBoot322Java17GradleGroovy.dto.ArticleForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {

    @GetMapping("/articles/new")
    public String newArticleForm(){
        System.out.println("/articles/new");
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        System.out.println("/articles/create");
        System.out.println(form.toString());
        return "";
    }
}
