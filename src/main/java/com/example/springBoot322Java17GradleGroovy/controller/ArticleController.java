package com.example.springBoot322Java17GradleGroovy.controller;

import com.example.springBoot322Java17GradleGroovy.dto.ArticleForm;
import com.example.springBoot322Java17GradleGroovy.entity.Article;
import com.example.springBoot322Java17GradleGroovy.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j //로깅을 위한 어노테이션
public class ArticleController {

    @Autowired//스프링 부트가 미리 생성해 놓은 객체를 가져다가 자동 연결
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticleForm(){
        //System.out.println("/articles/new");
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        //System.out.println("/articles/create");
//        System.out.println(form.toString());->로깅기능으로 대체
        log.info(form.toString());

        //1.Dto를 변환 Entity
        Article article = form.toEntity();
//        System.out.println(article.toString());
        log.info(article.toString());

        //2. Repository에게 Entity를 DB안에 저장하게 함
        Article saved = articleRepository.save(article);
//        System.out.println(saved.toString());
        log.info(saved.toString());

        return "";
    }

    @GetMapping("/articles/{id}")//해당 URL요청을 처리 선언
    public String show(@PathVariable Long id, Model model) {//URL에서 id를 변수로 가져옴
        log.info("id = " + id);

        //1. id로 데이터를 가져옴
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //2. 가져온 데이터를 모델에 등록
        model.addAttribute("article", articleEntity);

        //3. 보여줄 페이지를 설정
        return "articles/show";
    }
}
