package com.ashish.blogApp.service;

import com.ashish.blogApp.dao.TagRepository;
import com.ashish.blogApp.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService{
    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public List<Tag> findAllTag() {
             List<Tag> tags = tagRepository.findAll();
             return tags;
    }

    @Override
    public List<Tag> findAllTagByName(String name) {
        return tagRepository.findAllByName(name);
    }

    @Override
    public Tag findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }


}
