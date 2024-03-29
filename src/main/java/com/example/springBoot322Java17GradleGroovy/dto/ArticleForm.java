package com.example.springBoot322Java17GradleGroovy.dto;

import com.example.springBoot322Java17GradleGroovy.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor//생성자 자동생성
@ToString//toString 자동생성
public class ArticleForm {
    private Long id;
    private String title;
    private String content;

    /*public ArticleForm(String title, String content) {
        this.title = title;
        this.content = content;
    }*/

    /*@Override
    public String toString() {
        return "ArticleForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }*/

    public Article toEntity() {
        return new Article(id, title, content);
    }
}