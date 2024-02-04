package ru.itmo.wp.form;

import ru.itmo.wp.domain.Post;

import javax.validation.constraints.*;

public class PostData {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    private String title;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 65000)
    private String text;
    @NotNull(message = "You need to be logged for writing posts")
    private String jwt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Post toPost() {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        return post;
    }
}
