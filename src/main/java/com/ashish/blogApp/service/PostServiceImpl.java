package com.ashish.blogApp.service;

import com.ashish.blogApp.dao.PostRepository;
import com.ashish.blogApp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private TagService tagService;
    private CommentService commentService;
    private UserService userService;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post savePost(Post post) {
        Post dbPost = postRepository.saveAndFlush(post);
        return dbPost;
    }

    @Override
    public void publish(Post post) {
        postRepository.save(post);
    }

    @Override
    public List<Post> fetchAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Page<Post> findAll(int pageNo, int pageSize) {

        return postRepository.findAll(PageRequest.of(pageNo, pageSize));
    }

    @Override
    public Post findPostById(Integer id) {
        Optional<Post> result = postRepository.findById(id);
        Post post = null;
        if (result.isPresent()) {
            post = result.get();
        } else {
            throw new RuntimeException("There is no post of this id: " + id);
        }
        return post;
    }

    @Override
    public void deletePostById(Integer id) {
        Optional<Post> result = postRepository.findById(id);
        Post post = null;
        if (result.isPresent()) {
            post = result.get();
        } else {
            throw new RuntimeException("There is no post of this id: " + id);
        }
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> findAllPostOrderByPublishedAtDesc() {
        List<Post> posts = postRepository.findAllByOrderByPublishedAtDesc();
        return posts;
    }

    @Override
    public List<Post> findAllPostByTitleAsc() {
        List<Post> posts = postRepository.findAllByOrderByTitle();
        return posts;
    }

    @Override
    public List<Post> findAllPostBasedOnSearch(String searchedText) {
        List<Post> posts = postRepository.findAllPostBasedOnSearch(searchedText);
        return posts;
    }

    @Override
    public List<Post> findAllPostPublishedBetweenStartDateAndEndDate(String startDate, String endDate) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        if (endDate.equals("")) {
            System.out.println(formattedDateTime);
            endDate = formattedDateTime;
        }
        if (startDate.equals("")) {
            startDate = "0001-01-01 00:00:00";
        }
        return postRepository.findAllByPublishedAtBetween(startDate, endDate);
    }


    @Override
    public List<Post> findAllPostBasedOnSearchOrderByTitle(String searchedText) {
        return postRepository.findAllPostBasedOnSearchOrderByTitle(searchedText);
    }

    @Override
    public List<Post> findAllPostBasedOnSearchOrderByPublishedAt(String searchedText) {
        return postRepository.findAllPostBasedOnSearchOrderByPublishedAt(searchedText);
    }


    //pagination
    @Override
    public Page<Post> findPaginated(String searchedTextOnFilter, List<String> authorsOnFilter, List<String> tagsOnFilter, String startDateOnFilter, String endDateOnFilter, int pageNo, int pageSize) {
        return postRepository.findAllPostAfterApplyingFilters(searchedTextOnFilter, authorsOnFilter, tagsOnFilter, startDateOnFilter, endDateOnFilter, PageRequest.of(pageNo - 1, pageSize));
    }

    @Override
    public Page<Post> findPaginatedForAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.postRepository.findAll(pageable);
    }

}
