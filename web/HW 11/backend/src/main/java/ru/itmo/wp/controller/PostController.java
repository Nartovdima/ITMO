package ru.itmo.wp.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.exception.ValidationException;
import ru.itmo.wp.form.CommentData;
import ru.itmo.wp.form.PostData;
import ru.itmo.wp.form.validator.CommentDataCreationValidator;
import ru.itmo.wp.form.validator.PostDataCreateValidator;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final PostDataCreateValidator postDataCreateValidator;
    private final CommentDataCreationValidator commentDataCreationValidator;


    public PostController(PostService postService, CommentService commentService, PostDataCreateValidator postDataCreateValidator, CommentDataCreationValidator commentDataCreationValidator) {
        this.postService = postService;
        this.commentService = commentService;
        this.postDataCreateValidator = postDataCreateValidator;
        this.commentDataCreationValidator = commentDataCreationValidator;
    }

    @InitBinder({"PostData", "CommentData"})
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(postDataCreateValidator);
        binder.addValidators(commentDataCreationValidator);
    }


    @GetMapping("posts")
    public List<Post> findPosts() {
        return postService.findAll();
    }

    @GetMapping("post")
    public Post findPost(@RequestParam long postId) {
        return postService.findById(postId);
    }
    @PostMapping("posts")
    public void writePost(@RequestBody @Valid PostData postData, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        postService.makeRecord(postData);
    }
    @PostMapping("post/addComment")
    public void writeComment(@RequestBody @Valid CommentData commentData, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        commentService.createComment(commentData);
    }

    @GetMapping("post/getComments")
    public List<Comment> getComments(@RequestParam long postId) {
        return commentService.getCommentsByPostId(postId);
    }
}
