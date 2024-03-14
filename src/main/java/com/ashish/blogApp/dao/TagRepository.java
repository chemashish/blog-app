package com.ashish.blogApp.dao;

import com.ashish.blogApp.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
}
