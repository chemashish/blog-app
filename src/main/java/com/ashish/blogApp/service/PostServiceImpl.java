package com.ashish.blogApp.service;

import com.ashish.blogApp.dao.PostRepository;
import com.ashish.blogApp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Post> fetchAllPosts() {
         return postRepository.findAll();
    }

    @Override
    public Post findPostById(Integer id) {
        Optional<Post> result =  postRepository.findById(id);
        Post post = null;
        if(result.isPresent()){
            post = result.get();
        }else{
            throw new RuntimeException("There is no post of this id: "+id);
        }
        return post;
    }

    @Override
    public void deletePostById(Integer id) {
        Optional<Post> result =  postRepository.findById(id);
        Post post = null;
        if(result.isPresent()){
            post = result.get();
        }else{
            throw new RuntimeException("There is no post of this id: "+id);
        }
        postRepository.delete(post);
    }

}
