package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.form.CommentData;
import ru.itmo.wp.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
    }

    public void createComment(CommentData commentData) {
        Comment comment = new Comment();
        comment.setText(commentData.getText());
        comment.setUser(commentData.getUser());
        comment.setPost(commentData.getPost());

        commentRepository.save(comment);
    }
}
