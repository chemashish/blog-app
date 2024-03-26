package com.ashish.blogApp.restController;

import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.Tag;
import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.PostService;
import com.ashish.blogApp.service.TagService;
import com.ashish.blogApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostRestController {
    public static final int PAGE_SIZE = 10;
    private PostService postService;
    private TagService tagService;
    private UserService userService;

    @Autowired
    public PostRestController(PostService postService , TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;

    }
    @GetMapping("/posts")
    public List<Post> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size){

        Page<Post> thePage =postService.findAll(page,size);
        List<Post> posts =thePage.getContent();

        if(posts==null){
            throw new RuntimeException("There is no posts available");
        }
        return posts;
    }
    @GetMapping("/posts/{postId}")
    public Post getPostById(@PathVariable("postId") int postId){
        Post post = postService.findPostById(postId);
        if(post==null){
            throw new RuntimeException("No post available for this id - "+postId);
        }
        return post;
    }
    @PostMapping("/posts")
    public Post savePost(@RequestBody Post post){
        User author = userService.findUserByUserId(12);
        post.setId(0);
        post.setAuthor(author);
        Post dbPost = postService.savePost(post);
        return dbPost;
    }
    @PutMapping("/posts")
    public Post updatePost(@RequestBody Post post){
        Post dbPost =postService.savePost(post);
        return dbPost;
    }
    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable int postId){
        Post post = postService.findPostById(postId);
        System.out.println(post.getTitle());
        if(post == null){
            throw  new RuntimeException("No such post of id- "+postId);
        }
        postService.deletePostById(postId);
        return  "Deleted post of id "+ postId;
    }


    @GetMapping("/filter")
    public List<Post> findPaginated(@RequestParam(value = "authorsOnFilter", required = false) List<String> authorsOnFilter,
                                @RequestParam(value = "tagsOnFilter", required = false) List<String> tagsOnFilter,
                                @RequestParam(value = "startDate", required = false) String startDateOnFilter,
                                @RequestParam(value = "endDate", required = false) String endDateOnFilter,
                                @RequestParam(value = "selectedOption", defaultValue = "desc") String optionOnFilter,
                                @RequestParam(value = "searchedTextOnFilter", required = false) String searchedTextOnFilter,
                                @RequestParam(value = "authorsForRemainChecked", required = false) List<String> authorsForRemainChecked,
                                @RequestParam(value = "tagsForRemainChecked", required = false) List<String> tagsForRemainChecked,
                                @RequestParam(value = "searchedTextAfterFilter", required = false) String searchedTextAfterFilter,
                                @RequestParam(value = "pageNo", required = false) int currentPage,
                                Model model) {

        int pageSize = PAGE_SIZE;
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        if (startDateOnFilter.equals("")) {
            startDateOnFilter = "0000-00-00 00:00:00";
        }
        if (endDateOnFilter.equals("")) {
            endDateOnFilter = formattedDateTime;
        }
        HashMap<Integer, Tag> tagWithThierIds = new HashMap<>();
        HashMap<Integer, User> authorWithThierIds = new HashMap<>();
        List<User> authors = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        Page<Post> page = postService.findPaginated(searchedTextOnFilter, authorsOnFilter, tagsOnFilter, startDateOnFilter, endDateOnFilter, currentPage, PAGE_SIZE);
        List<Post> posts = page.getContent();
        return posts;
    }
}
