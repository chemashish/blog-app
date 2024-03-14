package com.ashish.blogApp.dao;

import com.ashish.blogApp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PostRepository extends JpaRepository<Post,Integer> {
}
