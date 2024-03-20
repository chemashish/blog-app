package com.ashish.blogApp.service;

import com.ashish.blogApp.entity.Post;
import com.ashish.blogApp.entity.Tag;
import com.ashish.blogApp.entity.User;

import java.util.List;

public interface TagService {
    List<Tag> findAllTag();
    List<Tag> findAllTagByName(String name);
    Tag findTagByName(String name);
    List<Post> findAllPostsOfParticularTag(String name);
}
