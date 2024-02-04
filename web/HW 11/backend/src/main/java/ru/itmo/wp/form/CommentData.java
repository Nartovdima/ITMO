package ru.itmo.wp.form;

import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentData {
    @NotNull(message = "You need to be logged for writing posts")
    private String jwt;
    private long postId;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 65000)
    private String text;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
