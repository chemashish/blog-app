package com.ashish.blogApp.service;

import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    User findUserByUserId(Integer id);
    List<User> findAllUser();
    List<Post> findPostsByAuthorName(String authorName);
    User findUserByUserName(String name);
    void saveUser(User user);
}
