package com.ashish.blogApp.securityConfig;

import com.ashish.blogApp.dao.UserRepository;
import com.ashish.blogApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepository.findUserByName(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found!!");
        }
        else{
            return  new  CustomUser(user) ;
        }
    }
}
