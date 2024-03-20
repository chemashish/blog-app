package com.ashish.blogApp.dao;

import com.ashish.blogApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByName(String name);
    //User findUserByNameBetween(String name , String startName , String endName);
}
