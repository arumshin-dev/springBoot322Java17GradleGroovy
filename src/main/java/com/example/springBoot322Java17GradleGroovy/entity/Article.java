package com.example.springBoot322Java17GradleGroovy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity//DB가 해당 객체를 인식 가능
public class Article {
    
    @Id//대표값 지정 PK
    @GeneratedValue//자동 생성 어노테이션
    private Long id;
    
    @Column//칼럼 필드 선언
    private String title;

    @Column
    private String content;

    //생성자 생성
    public Article(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
