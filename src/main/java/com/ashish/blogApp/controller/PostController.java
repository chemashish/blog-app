package com.ashish.blogApp.controller;

import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.Tag;
import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.PostService;
import com.ashish.blogApp.service.TagService;
import com.ashish.blogApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.List;

@Controller
public class PostController {
    public static final int PAGE_SIZE = 10;
    private PostService postService;
    private TagService tagService;
    private UserService userService;
    @Autowired
    public PostController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;

    }
    @GetMapping("/newpost")
    public String createPost(Model model) {
        Post post = new Post();
        String tagString = new String();
        List<Tag> tags = tagService.findAllTag();
        model.addAttribute("post", post);
        model.addAttribute("tagString", tagString);
        return "post";
    }
    @PostMapping("/publish")
    public String publishPost(HttpServletRequest request, @ModelAttribute("post") Post post, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        String tags = request.getParameter("tag");
        String tagWithoutBrackets = tags.replace("[", "").replace("]", "");
        List<String> tagsInPost = Arrays.asList(tagWithoutBrackets.split(","));
        List<Tag> tagsInDB = tagService.findAllTag();
        Set<String> tagsNameInDB = new HashSet<>();
        for (Tag tag : tagsInDB) {
            tagsNameInDB.add(tag.getName());
        }
        List<Tag> newTagsName = new ArrayList<>();
        post.setTags(null);
        for (String tagName : tagsInPost) {
            if (!tagsNameInDB.contains(tagName)) {
                Tag tag = new Tag(tagName);
                post.addTag(tag);
            } else {
                Tag newTag = tagService.findTagByName(tagName);
                newTag.setUpdatedAt(formattedDateTime);
                post.addTag(newTag);
            }
        }
        post.setPublished(true);
        if (post.getId() == 0) {
            post.setPublishedAt(formattedDateTime);
            post.setUpdatedAt(formattedDateTime);
        } else {
            post.setUpdatedAt(formattedDateTime);
        }
        String content = post.getContent();
        post.setExcerpt(content.length() > 30 ? content.substring(0, 31) : content);
        User user = userService.findUserByUserName(userDetails.getUsername());
        post.setAuthor(user);
        postService.publish(post);
        return "redirect:/";
    }
    @GetMapping("/")
    public String showAllPost(Model model) {
        int pageSize = PAGE_SIZE;
        int pageNo = 1;
        Page<Post> page = postService.findPaginatedForAll(pageNo, pageSize);
        List<Post> listPosts = page.getContent();
        List<Tag> tags = tagService.findAllTag();
        List<User> users = userService.findAllUser();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("posts", listPosts);
        model.addAttribute("tags", tags);
        model.addAttribute("users", users);
        model.addAttribute("tags", tags);
        model.addAttribute("option", "");
        model.addAttribute("searchedText", "");
        model.addAttribute("authorsForRemainChecked", users);
        model.addAttribute("tagsForRemainChecked", tags);
        model.addAttribute("authorsOnFilter", users);
        model.addAttribute("tagsOnFilter", tags);
        return "index";
    }

    @GetMapping("/post")
    public String showPost(@RequestParam("postId") int postId, Model model, HttpSession session, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            User currentUser = userService.findUserByUserName(username);
            model.addAttribute("currentUser", currentUser);
        }
        Post post = postService.findPostById(postId);
        model.addAttribute("post", post);
        return "complete_post";
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("postId") int postId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePostById(postId);
        List<Post> allPosts = postService.fetchAllPosts();
        model.addAttribute("posts", allPosts);
        return "index";
    }

    @GetMapping("/updatePost")
    public String updatePost(@RequestParam("postId") int postId, Model model) {
        Post post = postService.findPostById(postId);
        String tagString = new String();
        List<Tag> tagList = post.getTags();
        for (int i = 0; i < tagList.size(); i++) {
            if (i == tagList.size() - 1) {
                tagString += tagList.get(i);
            } else {
                tagString += tagList.get(i) + ",";
            }
        }
        model.addAttribute("post", post);
        model.addAttribute("tagString", tagString);
        return "post";
    }

    @GetMapping("/filter")
    public String addFilterWithPagination(@RequestParam(value = "authorsOnFilter", required = false) List<String> authorsOnFilter,
                                          @RequestParam(value = "tagsOnFilter", required = false) List<String> tagsOnFilter,
                                          @RequestParam(value = "startDate", required = false) String startDateOnFilter,
                                          @RequestParam(value = "endDate", required = false) String endDateOnFilter,
                                          @RequestParam(value = "selectedOption", defaultValue = "DESC") String optionOnFilter,
                                          @RequestParam(value = "searchedTextOnFilter", required = false) String searchedTextOnFilter,
                                          @RequestParam("authorsForRemainChecked") List<String> authorsForRemainChecked,
                                          @RequestParam("tagsForRemainChecked") List<String> tagsForRemainChecked,
                                          @RequestParam(value = "searchedTextAfterFilter", required = false) String searchedTextAfterFilter,
                                          Model model
    ) {

        int currentPage = 1;
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

        if (!(searchedTextOnFilter.isEmpty() && searchedTextAfterFilter.isEmpty()) && !(searchedTextOnFilter.equals(searchedTextAfterFilter))) {
            authorsOnFilter = null;
            tagsOnFilter = null;
        }
        List<User> authors = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        Page<Post> page = postService.findPaginated(searchedTextOnFilter, authorsOnFilter, tagsOnFilter, startDateOnFilter, endDateOnFilter, currentPage, PAGE_SIZE);
        List<Post> posts = page.getContent();
        if ((!searchedTextOnFilter.equals("")) && (authorsOnFilter == null) && (tagsOnFilter == null)) {
            for (Post post : posts) {
                List<Tag> tagsOfPost = post.getTags();
                User authorOfPost = post.getAuthor();
                if (!authorWithThierIds.containsKey(authorOfPost.getId())) {
                    authorWithThierIds.put(authorOfPost.getId(), authorOfPost);
                }
                for (Tag tag : tagsOfPost) {
                    if (!tagWithThierIds.containsKey(tag.getId())) {
                        tagWithThierIds.put(tag.getId(), tag);
                    }
                }
            }
            for (Map.Entry<Integer, User> entry : authorWithThierIds.entrySet()) {
                authors.add(entry.getValue());
            }
            for (Map.Entry<Integer, Tag> entry : tagWithThierIds.entrySet()) {
                tags.add(entry.getValue());
            }
        } else {
            for (String authorAfterFilter : authorsForRemainChecked) {
                User authorOfPost = userService.findUserByUserName(authorAfterFilter);
                authors.add(authorOfPost);
            }
            for (String tagAfterFilter : tagsForRemainChecked) {
                Tag tagOfPost = tagService.findTagByName(tagAfterFilter);
                tags.add(tagOfPost);
            }
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("posts", posts);
        model.addAttribute("users", authors);
        model.addAttribute("tags", tags);
        model.addAttribute("option", "");
        model.addAttribute("searchedText", searchedTextOnFilter);
        model.addAttribute("authorsOnFilter", authorsOnFilter);
        model.addAttribute("tagsOnFilter", tagsOnFilter);
        model.addAttribute("startDate", startDateOnFilter);
        model.addAttribute("endDate", endDateOnFilter);
        return "index";
    }


    @GetMapping("/page")
    public String findPaginated(@RequestParam(value = "authorsOnFilter", required = false) List<String> authorsOnFilter,
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
        for (String authorAfterFilter : authorsForRemainChecked) {
            User authorOfPost = userService.findUserByUserName(authorAfterFilter);
            authors.add(authorOfPost);
        }
        for (String tagAfterFilter : tagsForRemainChecked) {
            Tag tagOfPost = tagService.findTagByName(tagAfterFilter);
            tags.add(tagOfPost);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("posts", posts);
        model.addAttribute("users", authors);
        model.addAttribute("tags", tags);
        model.addAttribute("option", "");
        model.addAttribute("searchedText", searchedTextOnFilter);
        model.addAttribute("authorsOnFilter", authors);
        model.addAttribute("tagsOnFilter", tags);
        return "index";
    }
}
