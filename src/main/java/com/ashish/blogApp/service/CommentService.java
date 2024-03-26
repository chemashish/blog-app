package com.ashish.blogApp.service;

import com.ashish.blogApp.entity.Comment;
import jakarta.persistence.criteria.CriteriaBuilder;

public interface CommentService {
    Comment findCommentFindByCommentId(Integer id);
    void deleteCommentByCommentId(Comment comment);
    void saveComment(Comment comment);
    Comment save(Comment comment);
}
