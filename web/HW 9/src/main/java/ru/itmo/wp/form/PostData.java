package ru.itmo.wp.form;

import ru.itmo.wp.domain.Post;
import javax.validation.constraints.*;
import java.util.Set;

public class PostData {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    private String title;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 65000)
    private String text;

    @NotNull
    @Size(max = 2000)
    @Pattern(regexp = "[a-z\\s]*", message = "Only lowercase latin letters and whitespaces expected")
    private String tags;

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

    public String getTags() {
        return tags;
    }

    public Post toPost() {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        return post;
    }

    public Set<String> getParsedTags() {
        return Set.of(getTags().trim().split("\\s"));
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
