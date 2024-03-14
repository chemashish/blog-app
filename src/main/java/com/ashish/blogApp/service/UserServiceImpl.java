package com.ashish.blogApp.service;

import com.ashish.blogApp.dao.UserRepository;
import com.ashish.blogApp.entity.User;
import org.springframework.stereotype.Service;

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
}
