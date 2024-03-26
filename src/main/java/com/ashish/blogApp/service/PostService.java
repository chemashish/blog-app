package com.ashish.blogApp.service;

import com.ashish.blogApp.entity.Post;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    Post savePost(Post post);
    void publish(Post post);
    List<Post> fetchAllPosts();
    Page<Post> findAll(int pageNo, int pageSize);
    Post findPostById(Integer id);
    void deletePostById(Integer id);
    List<Post> findAllPostOrderByPublishedAtDesc();
    List<Post> findAllPostByTitleAsc();
    List<Post> findAllPostBasedOnSearch(String searchedText);
    List<Post> findAllPostPublishedBetweenStartDateAndEndDate(String startDate, String endDate);
    List<Post> findAllPostBasedOnSearchOrderByTitle(String searchedText);
    List<Post> findAllPostBasedOnSearchOrderByPublishedAt(String searchedText);
    Page<Post> findPaginated(String searchedTextOnFilter, List<String> authorsOnFilter, List<String> tagsOnFilter, String startDateOnFilter, String endDateOnFilter, int pageNo, int pageSize);
    Page<Post> findPaginatedForAll(int pageNo, int pageSize);
}
