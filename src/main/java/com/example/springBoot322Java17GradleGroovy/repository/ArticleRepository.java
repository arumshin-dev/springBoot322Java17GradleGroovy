package com.example.springBoot322Java17GradleGroovy.repository;

import com.example.springBoot322Java17GradleGroovy.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Override
    ArrayList<Article> findAll();
}
