package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.form.CommentData;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;
    private final CommentService commentService;

    public PostPage(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }
    @Guest
    @GetMapping(value={"/post/{id}", "/post"})
    public String posts(@PathVariable(required = false) String id, Model model, HttpSession httpSession) {
        try{
            model.addAttribute("post", postService.findById(Long.parseLong(id)));
            if (getUser(httpSession) != null) {
                model.addAttribute("commentForm", new CommentData());
            }
        } catch (NumberFormatException ignored) {
            //No op
        }

        return "PostPage";
    }
    @Guest
    @PostMapping(value={"/post/{id}", "/post"})
    public String commentCreator(@Valid @ModelAttribute("commentForm") CommentData commentForm,
                                 BindingResult bindingResult,
                                 HttpSession httpSession, @PathVariable(required = false) String id) {
        Post post = null;
        try{
            post = postService.findById(Long.parseLong(id));
        } catch (NumberFormatException ignored) {
            //No op
        }

        if (post == null) {
            putMessage(httpSession, "No such post.");
            return "redirect:";
        }

        if (bindingResult.hasErrors()) {
            putMessage(httpSession, "Empty comment");
            return "redirect:/post/" + id;
        }
        User currentUser = getUser(httpSession);
        if (currentUser == null) {
            putMessage(httpSession, "Enter into the website");
            return "redirect:/enter";
        }
        commentForm.setPost(post);
        commentForm.setUser(currentUser);
        commentService.createComment(commentForm);
        putMessage(httpSession, "Comment successfully created!");
        return "redirect:/post/" + id;
    }
}
