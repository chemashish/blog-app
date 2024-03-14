package com.ashish.blogApp.controller;

import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.Tag;
import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.PostService;
import com.ashish.blogApp.service.PostServiceImpl;
import com.ashish.blogApp.service.TagService;
import com.ashish.blogApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.security.Timestamp;
import java.util.*;
@Controller
public class PostController {
    private PostService postService;
    private TagService tagService;
    private UserService userService;
    @Autowired
    public PostController(PostService postService , TagService tagService,UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }
    @GetMapping("/newpost")
    public String createPost(Model model){
        Post post = new Post();
        String tagString = new String();
        model.addAttribute("post",post);
        model.addAttribute("tagString",tagString);
        return "post";
    }
    @PostMapping("/publish")
    public String publishPost(HttpServletRequest request , @ModelAttribute("post") Post post,Model model) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        String tags = request.getParameter("tag");
        String tagWithoutBrackets = tags.replace("[","").replace("]","");
        List<String> tagsInPost = Arrays.asList(tagWithoutBrackets.split(","));
        List<Tag> tagsInDB = tagService.findAllTag();
        Set<String> tagsNameInDB =new HashSet<>();
        for(Tag tag : tagsInDB){
            tagsNameInDB.add(tag.getName());
        }
        List<Tag> newTagsName =new ArrayList<>();
        post.setTags(null);
        for(String tagName: tagsInPost){
            if(!tagsNameInDB.contains(tagName)){
                Tag tag = new Tag(tagName);
                post.addTag(tag);
            }else {
                Tag newTag = tagService.findTagByName(tagName);
                newTag.setUpdatedAt(formattedDateTime);
                post.addTag(newTag);
            }
        }
        post.setPublished(true);
        if(post.getId()==0){
            post.setUpdatedAt(formattedDateTime);
        }else{
            post.setPublishedAt(formattedDateTime);
            post.setUpdatedAt(formattedDateTime);
        }
        String content = post.getContent();
        post.setExcerpt(content.length()>30?content.substring(0,31):content);
        User user = userService.findUserByUserId(2);
        post.setAuthor(user);
        postService.publish(post);
        return "redirect:/";
    }
    @GetMapping("/")
    public String showAllPost(Model model){
        List<Post> allPosts = postService.fetchAllPosts();
        model.addAttribute("allposts",allPosts);
        return "index";
    }
    @GetMapping("/post")
    public String showPost(@RequestParam("postId") int postId, Model model){
        Post post = postService.findPostById(postId);
        model.addAttribute("post",post);
        return "complete_post";
    }
    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("postId") int postId,Model model){
        postService.deletePostById(postId);
        List<Post> allPosts = postService.fetchAllPosts();
        model.addAttribute("allposts",allPosts);
        return "index";
    }
    @GetMapping("/updatePost")
    public String updatePost(@RequestParam("postId") int postId,Model model){
         Post post = postService.findPostById(postId);
         String tagString =new String();
         List<Tag> tagList = post.getTags();
         for(int i=0;i<tagList.size();i++){
             if(i==tagList.size()-1){
                 tagString+=tagList.get(i);
             }else{
                 tagString+=tagList.get(i)+",";
             }
         }
         model.addAttribute("post",post);
         model.addAttribute("tagString",tagString);
         return "post";
    }
}
