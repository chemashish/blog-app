package com.ashish.blogApp.controller;

import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.Tag;
import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.PostService;
import com.ashish.blogApp.service.PostServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class PostController {
    private PostServiceImpl postServiceImpl;
    @Autowired
    public PostController(PostServiceImpl postServiceImpl) {
        this.postServiceImpl = postServiceImpl;
    }
    @GetMapping("/newpost")
    public String createPost(Model model){
        Post post = new Post();
        model.addAttribute("post",post);
        return "post";
    }
    @PostMapping("/publish")
    public String publishPost(HttpServletRequest request, @ModelAttribute("post") Post post) {
        User user= new User("shubham","shubahm@gmail.com","football");
        post.setAuthor(user);
        String tagList=request.getParameter("tags");
        List<String> items = Arrays.asList(tagList.split("\\s*,\\s*"));
        for(String item:items){
            Tag tag= new Tag(item);
            post.addTag(tag);
        }
        postServiceImpl.publish(post);
        return "confirmationOfPost";
    }

}
