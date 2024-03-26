package com.ashish.blogApp.restController;

import com.ashish.blogApp.entity.Comment;
import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.CommentService;
import com.ashish.blogApp.service.PostService;
import com.ashish.blogApp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    private PostService postService;
    private UserService userService;
    private CommentService commentService;
    public CommentRestController(PostService postService , UserService userService , CommentService commentService){
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/posts/{postId}/comments")
    public List<Comment> getAllCommentsOfPost(@PathVariable int postId){
        Post post = postService.findPostById(postId);
        if(post == null){
            throw new RuntimeException("there is no post of this id-"+postId);
        }
        return post.getComments();
    }

    @PostMapping("/posts/{postId}/comments")
    public List<Comment> saveComment(@PathVariable int postId, @RequestBody Comment comment, @AuthenticationPrincipal UserDetails userDetails){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        Post post = postService.findPostById(postId);
        comment.setId(0);
        User user = null;
        if(userDetails !=null){
            user = userService.findUserByUserName(userDetails.getUsername());
        }
        comment.setName(user.getName());
        comment.setEmail(user.getEmail());
        comment.setCreatedAt(formattedDateTime);
        comment.setUpdatedAt(formattedDateTime);
        post.addComment(comment);
        Post dbPost = postService.savePost(post);
        return dbPost.getComments();
    }
    @PutMapping("/comments")
    public Comment updateComment(@RequestBody Comment comment){
        Comment dbComment = commentService.save(comment);
        return dbComment;
    }

    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable int commentId){
        Comment comment = commentService.findCommentFindByCommentId(commentId);
        if(comment == null){
            throw new RuntimeException("There is no comment with this id-"+commentId);
        }
        commentService.deleteCommentByCommentId(comment);
        return "Delete comment with id-"+commentId;
    }
}
