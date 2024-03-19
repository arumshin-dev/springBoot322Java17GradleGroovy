package com.example.springBoot322Java17GradleGroovy.repository;

import com.example.springBoot322Java17GradleGroovy.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository {
    @Query(value =
            "SELECT * " +
                    "FROM comment " +
                    "WHERE article_id = :articleId",
            nativeQuery = true)
    List<Comment> findByArticleId(@Param("articleId") Long articleId);
    List<Comment> findByNickname(String nickname);
}