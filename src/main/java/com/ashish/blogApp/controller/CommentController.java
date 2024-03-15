package com.ashish.blogApp.controller;

import com.ashish.blogApp.entity.Comment;
import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.CommentService;
import com.ashish.blogApp.service.PostService;
import com.ashish.blogApp.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CommentController {
    private PostService postService;
    private UserService userService;
    private CommentService commentService;
    public CommentController(PostService postService , UserService userService , CommentService commentService){
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }
    @GetMapping("/commentOnPost")
    public String comment(@RequestParam("postId") int postId , Model model){
        Post post = postService.findPostById(postId);
        model.addAttribute("post",post);
        model.addAttribute("comments",post.getComments());
        return "postWithComments";
    }
    @PostMapping("/addComment{postId}")
    public String addComment(@PathVariable("postId") int postId, @RequestParam("comment") String  commentFromPost , Model model){
        //Login
        User user = userService.findUserByUserId(8);
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        Post post = postService.findPostById(postId);
        Comment comment = new Comment(commentFromPost);
        comment.setName(user.getName());
        comment.setEmail(user.getEmail());
        comment.setCreatedAt(formattedDateTime);
        comment.setUpdatedAt(formattedDateTime);
        post.addComment(comment);
        postService.publish(post);
        model.addAttribute("post",post);
        model.addAttribute("comments",post.getComments());
//        for(Comment com: post.getComments()){
//            System.out.println(com);
//        }
        return "postWithComments";
    }
    @GetMapping("/deletecomment/postId/{postId}/commentId/{commentId}")
    public String addDelete(@PathVariable("postId") int postId,@PathVariable("commentId") int commentId, Model model){
        Post post = postService.findPostById(postId);
        Comment comment = commentService.findCommentFindByCommentId(commentId);
        commentService.deleteCommentByCommentId(comment);
        model.addAttribute("post", post);
        model.addAttribute("comments",post.getComments());
        return "postWithComments";
    }
}
