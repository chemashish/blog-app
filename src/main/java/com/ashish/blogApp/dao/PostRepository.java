package com.ashish.blogApp.dao;

import com.ashish.blogApp.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findAllByOrderByPublishedAtDesc();

    List<Post> findAllByOrderByTitle();

    @Query("SELECT p FROM Post p WHERE  p.author.name ILIKE  %:searchedText% OR p.title ilike %:searchedText% OR p.content ilike%:searchedText%")
    List<Post> findAllPostBasedOnSearch(String searchedText);
    @Query("SELECT p FROM Post p WHERE  p.author.name ILIKE  %:searchedText% OR p.title ilike %:searchedText% OR p.content ilike%:searchedText% ORDER BY p.publishedAt desc ")
    List<Post> findAllPostBasedOnSearchOrderByPublishedAt(String searchedText);

    @Query("SELECT p FROM Post p WHERE  p.author.name ILIKE  %:searchedText% OR p.title ilike %:searchedText% OR p.content ilike%:searchedText% ORDER BY p.title")
    List<Post> findAllPostBasedOnSearchOrderByTitle(String searchedText);

    List<Post> findAllByPublishedAtBetween(String startDate, String endDate);

    @Query("SELECT DISTINCT p From Post p " +
            "JOIN fetch p.tags t WHERE (:searchedText IS NULL OR p.author.name ILIKE  %:searchedText%" +
            " OR p.title ilike %:searchedText% OR p.content ilike %:searchedText% ) " +
            "and p.publishedAt between :startDate and :endDate " +
            "and (:authors IS NULL OR p.author.name IN :authors) " +
            "and (:tags IS NULL OR t.name IN :tags) " +
            "order by p.publishedAt DESC ")
    Page<Post> findAllPostAfterApplyingFilters(String searchedText, List<String> authors, List<String> tags, String startDate, String endDate,Pageable pageable);
}
