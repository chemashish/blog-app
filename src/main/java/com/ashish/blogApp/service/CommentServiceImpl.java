package com.ashish.blogApp.service;

import com.ashish.blogApp.dao.CommentRepository;
import com.ashish.blogApp.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment findCommentFindByCommentId(Integer id) {
        Optional<Comment> result = commentRepository.findById(id);
        Comment comment = null;
        if (result.isPresent()) {
            comment = result.get();
        } else {
            throw new RuntimeException("Comment of this not found");
        }
        return comment;
    }

    @Override
    public void deleteCommentByCommentId(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment save(Comment comment) {
        Comment dbComment = commentRepository.save(comment);
        return dbComment;
    }
}
