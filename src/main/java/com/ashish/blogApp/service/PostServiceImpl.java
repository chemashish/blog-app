package com.ashish.blogApp.service;

import com.ashish.blogApp.dao.PostRepository;
import com.ashish.blogApp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService{
    private PostRepository postRepository;
    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void publish(Post post) {
         postRepository.save(post);
    }

}
