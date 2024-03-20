package com.ashish.blogApp.dao;

import com.ashish.blogApp.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface TagRepository extends JpaRepository<Tag,Integer> {
    List<Tag> findAllByName(String name);
    Tag findTagByName(String Name);

}
