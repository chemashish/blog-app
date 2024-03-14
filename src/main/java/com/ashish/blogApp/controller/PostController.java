package com.ashish.blogApp.controller;

import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.Tag;
import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.PostService;
import com.ashish.blogApp.service.PostServiceImpl;
import com.ashish.blogApp.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class PostController {
    private PostService postService;
    //private TagService tagService;
    @Autowired
    public PostController(PostService postService , TagService service) {
        this.postService = postService;
        //this.tagService = tagService;
    }
    @GetMapping("/newpost")
    public String createPost(Model model){
        Post post = new Post();
        model.addAttribute("post",post);
        return "post";
    }
    @PostMapping("/publish")
    public String publishPost(@ModelAttribute("post") Post post) {
//        List<Tag> tagsInDB = tagService.findAllTag();
//        Set<Tag> tags1 = new HashSet<>(tagsInDB);
//        List<Tag> tagsInPost = post.getTags();
//        List<Tag> newTags =new ArrayList<>();
//        for(Tag tag: tagsInPost){
//            if(!tags1.contains(tag)){
//                newTags.add(tag);
//            }
//        }
//        post.setTags(newTags);
//        System.out.println(post.getTags());
        User user= new User("rahul","rahul@gmail.com","12345");
        post.setAuthor(user);
        postService.publish(post);
        return "confirmationOfPost";
    }

}
