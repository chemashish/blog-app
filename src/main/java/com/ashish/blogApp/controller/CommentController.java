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
import org.springframework.web.bind.annotation.*;

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
        Comment comment = new Comment();
        model.addAttribute("comment",comment);
        return "postWithComments";
    }
    @PostMapping("/addComment{postId}")
    public String addComment(@PathVariable("postId") int postId, @ModelAttribute("comment") Comment comment, Model model){
        //Login
        User user = userService.findUserByUserId(8);
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        Post post = postService.findPostById(postId);
        if(comment.getId()==0){
            comment.setName(user.getName());
            comment.setEmail(user.getEmail());
            comment.setCreatedAt(formattedDateTime);
            comment.setUpdatedAt(formattedDateTime);
            post.addComment(comment);
            postService.publish(post);
        }else{
            comment.setName(user.getName());
            comment.setEmail(user.getEmail());
            comment.setUpdatedAt(formattedDateTime);
            commentService.saveComment(comment);
        }

        model.addAttribute("post",post);
        model.addAttribute("comments",post.getComments());
        return "postWithComments";
    }
    @GetMapping("/deletecomment/postId/{postId}/commentId/{commentId}")
    public String deleteComment(@PathVariable("postId") int postId,@PathVariable("commentId") int commentId, Model model){
        Post post = postService.findPostById(postId);
        Comment comment = commentService.findCommentFindByCommentId(commentId);
        commentService.deleteCommentByCommentId(comment);
        Comment  commentForModel = new Comment();
        model.addAttribute("post", post);
        model.addAttribute("comment",commentForModel);
        return "postWithComments";
    }
    @GetMapping("/updatecomment/postId/{postId}/commentId/{commentId}")
    public String updateComment(@PathVariable("postId") int postId,@PathVariable("commentId") int commentId, Model model){
        Post post = postService.findPostById(postId);
        Comment comment = commentService.findCommentFindByCommentId(commentId);
        model.addAttribute("post", post);
        model.addAttribute("comment",comment);
        return "postWithComments";
    }
}
