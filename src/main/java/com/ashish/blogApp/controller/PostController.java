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
    private TagService tagService;
    @Autowired
    public PostController(PostService postService , TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }
    @GetMapping("/newpost")
    public String createPost(Model model){
        Post post = new Post();
        model.addAttribute("post",post);
        return "post";
    }
    @PostMapping("/publish")
    public String publishPost(HttpServletRequest request , @ModelAttribute("post") Post post) {
        String tags = request.getParameter("tag");
        List<String> tagsInPost = Arrays.asList(tags.split(","));
        List<Tag> tagsInDB = tagService.findAllTag();
        Set<String> tagsNameInDB =new HashSet<>();
        for(Tag tag : tagsInDB){
            tagsNameInDB.add(tag.getName());
        }
        //List<Tag> newTagsName =new ArrayList<>();
        post.setTags(null);
        for(String tag: tagsInPost){
            if(!tagsNameInDB.contains(tag)){
                Tag newTag = new Tag(tag);
                post.addTag(newTag);
            }else {
                Tag newTag = tagService.findTagByName(tag);
                post.addTag(newTag);
            }
        }
        System.out.println(post.getTags());

        User user= new User("vishwa","vishwa@gmail.com","12345");
        post.setAuthor(user);
        postService.publish(post);
        return "confirmationOfPost";
    }


}
