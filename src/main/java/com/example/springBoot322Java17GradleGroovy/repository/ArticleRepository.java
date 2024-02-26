package com.example.springBoot322Java17GradleGroovy.repository;

import com.example.springBoot322Java17GradleGroovy.entity.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {

}
