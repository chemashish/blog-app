package com.ashish.blogApp.service;

import com.ashish.blogApp.entity.User;

public interface UserService {
    User findUserByUserId(Integer id);
}
