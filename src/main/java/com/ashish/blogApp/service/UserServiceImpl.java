package com.ashish.blogApp.service;

import com.ashish.blogApp.dao.UserRepository;
import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public User findUserByUserId(Integer id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;
        if(result.isPresent()){
            user = result.get();
        }else{
            throw new RuntimeException();
        }
        return user;
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }
    @Override
    public List<Post> findPostsByAuthorName(String authorName) {
        User author = userRepository.findUserByName(authorName);
        return author.getPosts();
    }
    @Override
    public User findUserByUserName(String name) {
        User user= userRepository.findUserByName(name);
        return user;
    }
}
