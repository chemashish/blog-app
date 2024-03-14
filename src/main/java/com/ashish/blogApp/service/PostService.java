package com.ashish.blogApp.service;

import com.ashish.blogApp.entity.Post;

import java.util.List;

public interface PostService {
    void publish(Post post);
    List<Post> fetchAllPosts();
    Post findPostById(Integer id);
    void deletePostById(Integer id);
}
