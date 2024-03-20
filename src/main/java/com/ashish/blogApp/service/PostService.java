package com.ashish.blogApp.service;

import com.ashish.blogApp.entity.Post;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface PostService {
    void publish(Post post);
    List<Post> fetchAllPosts();
    Post findPostById(Integer id);
    void deletePostById(Integer id);
    List<Post> findAllPostOrderByPublishedAtDesc();

    List<Post> findAllPostByTitleAsc();

    List<Post> findAllPostBasedOnSearch(String searchedText);

    List<Post> findAllPostPublishedBetweenStartDateAndEndDate(String startDate , String endDate);

    List<Post> findAllPostAfterApplyingFilterWithoutsorting(String searchedText , List<String> authors, List<String> tags ,String startDate ,String endDate);

    List<Post> findAllPostBasedOnSearchOrderByTitle(String searchedText);

    List<Post> findAllPostBasedOnSearchOrderByPublishedAt(String searchedText);
}
