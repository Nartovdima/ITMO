package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.form.PostData;
import ru.itmo.wp.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final JwtService jwtService;

    public PostService(PostRepository postRepository, JwtService jwtService) {
        this.postRepository = postRepository;
        this.jwtService = jwtService;
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    public Post findById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void makeRecord(PostData postData) {
        Post post = new Post();
        post.setTitle(postData.getTitle());
        post.setText(postData.getText());
        post.setUser(jwtService.find(postData.getJwt()));
        postRepository.save(post);
    }
}
