package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.form.CommentData;
import ru.itmo.wp.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService, JwtService jwtService, PostService postService1) {
        this.commentRepository = commentRepository;
        this.jwtService = jwtService;
        this.postService = postService1;
    }

    public void createComment(CommentData commentData) {
        Comment comment = new Comment();
        comment.setText(commentData.getText());
        comment.setUser(jwtService.find(commentData.getJwt()));
        comment.setPost(postService.findById(commentData.getPostId()));

        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(long postId) {
        return commentRepository.getCommentsByPostIdOrderByIdDesc(postId);
    }
}
