package com.ashish.blogApp.controller;

import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.Tag;
import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.PostService;
import com.ashish.blogApp.service.TagService;
import com.ashish.blogApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.List;

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
        List<Tag> tags = tagService.findAllTag();
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
            post.setPublishedAt(formattedDateTime);
            post.setUpdatedAt(formattedDateTime);
        }else{
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
        List<Post> posts = postService.fetchAllPosts();
        List<Tag> tags = tagService.findAllTag();
        List<User> users= userService.findAllUser();
        model.addAttribute("posts",posts);
        model.addAttribute("tags",tags);
        model.addAttribute("users", users);
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
        model.addAttribute("posts",allPosts);
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
//    @PostMapping("/sortPost")
//    public String sortPost(@RequestParam("selectedOption") String option,
//                           @ModelAttribute("searchedText") String searchedText,
//                           Model model){
//        System.out.println(searchedText);
//        List<Post> posts = postService.findAllPostBasedOnSearch(searchedText);
//        if(option.equals("date")){
//            posts.sort(Comparator.comparing(Post::getPublishedAt).reversed());
//            model.addAttribute("posts",posts);
//        }else if(option.equals("title")){
//            posts.sort(Comparator.comparing(Post::getTitle));
//            model.addAttribute("posts",posts);
//        }else {
//            List<Post> allPosts = postService.fetchAllPosts();
//            model.addAttribute("posts",allPosts);
//        }
//        return "index";
//    }


//    @PostMapping("/searchPost")
//    public String searchPost(@RequestParam("searchedText") String searchedText, Model model){
//        List<Post> posts = postService.findAllPostBasedOnSearch(searchedText);
//        model.addAttribute("searchedText",searchedText);
//        model.addAttribute("posts",posts);
//        return "index";
//    }
//    @GetMapping("/filter")
//    public String filterPost(@RequestParam(value = "author",required = false) Set<String> authorsAfterFilter,
//                             @RequestParam(value = "tags", required = false) Set<String> tagsAfterFilter,
//                             @RequestParam(value = "startDate",required = false) String startDate,
//                             @RequestParam(value = "endDate",required = false) String endDate,
//                             Model model){
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDateTime = currentDateTime.format(formatter);
//        HashMap<Integer, Post> postsWithTheirIds = new HashMap<>();
//        if(!(authorsAfterFilter==null)){
//            for(String author : authorsAfterFilter){
//                List<Post> posts = userService.findPostsByAuthorName(author);
//                for(Post post : posts){
//                    Integer idOfPost = post.getId();
//                    if(!postsWithTheirIds.containsKey(idOfPost)){
//                        postsWithTheirIds.put(idOfPost, post);
//                    }
//                }
//            }
//        }
//        if(!(tagsAfterFilter ==null)){
//            for(String tag : tagsAfterFilter){
//                List<Post> posts = tagService.findAllPostsOfParticularTag(tag);
//                for(Post post : posts){
//                    Integer idOfPost = post.getId();
//                    if(!postsWithTheirIds.containsKey(idOfPost)){
//                        postsWithTheirIds.put(idOfPost, post);
//                    }
//                }
//            }
//        }
//
//        if(!(startDate.equals("") && endDate.equals(""))){
//            List<Post> postsOfParticularDateRange  = postService.findAllPostPublishedBetweenStartDateAndEndDate(startDate,endDate);
//            for(Post post : postsOfParticularDateRange){
//                if(!(postsWithTheirIds.containsKey(post.getId()))){
//                    postsWithTheirIds.put(post.getId(), post);
//                }
//            }
//        }
//        if(startDate.equals("")){
//              startDate = "0000-00-00 00:00:00";
//        }
//        if (endDate.equals("")){
//            endDate  = formattedDateTime;
//        }
//
//        for(Map.Entry<Integer, Post> entry : postsWithTheirIds.entrySet()){
//            String publishedDataOfPost = entry.getValue().getPublishedAt();
//            if(!(( publishedDataOfPost.compareTo(startDate) > 0) && (publishedDataOfPost.compareTo(endDate) <0 ))){
//               postsWithTheirIds.remove(entry.getKey());
//            }
//        }
//
//        List<Post> posts = new ArrayList<>(postsWithTheirIds.values());
//        List<Tag> tags = tagService.findAllTag();
//        List<User> users= userService.findAllUser();
//        model.addAttribute("posts",posts);
//        model.addAttribute("tags",tags);
//        model.addAttribute("users", users);
//        model.addAttribute("posts" ,posts);
//        return "index";
//    }

    @PostMapping("/filter")
    public String addFilter(@RequestParam(value = "authorsOnFilter",required = false) List<String> authorsOnFilter,
                            @RequestParam(value = "tagsOnFilter",required = false) List<String> tagsOnFilter,
                            @RequestParam(value = "startDate",required = false) String startDateOnFilter,
                            @RequestParam(value = "endDate",required = false) String endDateOnFilter,
                            @RequestParam(value = "selectedOption",required = false) String optionOnFilter,
                            @RequestParam(value = "searchedText",required = false) String searchedTextOnFilter,
                            @RequestParam("authorsForRemainChecked") List<String> authorsForRemainChecked,
                            @RequestParam("tagsForRemainChecked") List<String> tagsForRemainChecked,
                            Model model
                            )
    {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        if(startDateOnFilter.equals("")){
            startDateOnFilter="0000-00-00 00:00:00";
        }
        if(endDateOnFilter.equals("")){
            endDateOnFilter = formattedDateTime;
        }
        List<Post> posts = new ArrayList<>();
        HashMap< Integer , Tag > tagWithThierIds = new HashMap<>();
        HashMap< Integer , User> authorWithThierIds = new HashMap<>();

        List<User> authors = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        if( (!searchedTextOnFilter.equals("")) && (authorsOnFilter == null) && (tagsOnFilter ==null)){
            if(optionOnFilter.equals("date")){
                posts = postService.findAllPostBasedOnSearchOrderByPublishedAt(searchedTextOnFilter);
            }else if(optionOnFilter.equals("title")){
                posts = postService.findAllPostBasedOnSearchOrderByTitle(searchedTextOnFilter);
            }else{
                posts = postService.findAllPostBasedOnSearch(searchedTextOnFilter);
            }
            for(Post post : posts){
                List<Tag> tagsOfPost = post.getTags();
                User authorOfPost = post.getAuthor();
                if(!authorWithThierIds.containsKey(authorOfPost.getId())){
                    authorWithThierIds.put(authorOfPost.getId(),authorOfPost);
                }
                for(Tag tag : tagsOfPost){
                    if(!tagWithThierIds.containsKey(tag.getId())){
                        tagWithThierIds.put(tag.getId(), tag);
                    }
                }
            }

            for(Map.Entry<Integer,User> entry : authorWithThierIds.entrySet()){
                authors.add(entry.getValue());
            }
            for(Map.Entry<Integer,Tag> entry: tagWithThierIds.entrySet()){
                tags.add(entry.getValue());
            }
        }
        else{
            posts = postService.findAllPostAfterApplyingFilterWithoutsorting(searchedTextOnFilter,authorsOnFilter,tagsOnFilter,startDateOnFilter,endDateOnFilter);
            for(String authorAfterFilter: authorsForRemainChecked){
                User authorOfPost = userService.findUserByUserName(authorAfterFilter);
                authors.add(authorOfPost);
            }
            for(String tagAfterFilter: tagsForRemainChecked ){
                Tag tagOfPost  = tagService.findTagByName(tagAfterFilter);
                tags.add(tagOfPost);
            }

        }
        model.addAttribute("users", authors);
        model.addAttribute("tags",tags);
        model.addAttribute("option","");
        model.addAttribute("searchedText",searchedTextOnFilter);
        model.addAttribute("posts",posts);
        model.addAttribute("authorsOnFilter",authorsOnFilter);
        model.addAttribute("tagsOnFilter",tagsOnFilter);
        return "index";
    }
}
