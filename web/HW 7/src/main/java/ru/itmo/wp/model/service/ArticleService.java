package ru.itmo.wp.model.service;


import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void makeRecord(Article article) throws ValidationException {
        validateArticle(article);
        articleRepository.save(article);
    }

    private void validateArticle(Article article) throws ValidationException {
        if (Strings.isNullOrEmpty(article.getTitle())) {
            throw new ValidationException("Title is required");
        }
        if (article.getTitle().length() >= 255) {
            throw new ValidationException("Title is too long");
        }
        if (Strings.isNullOrEmpty(article.getText())) {
            throw new ValidationException("Text is required");
        }
        if (article.getText().length() >= 20000) {
            throw new ValidationException("Text is too long");
        }
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findUserArticles(long id) {
        return articleRepository.findUserArticles(id);
    }

    public void changeVisibility(long id, boolean visibility, User user) throws ValidationException {
        validateOwnership(id, user.getId());
        articleRepository.changeVisibility(id, visibility);
    }

    private void validateOwnership(long articleId, long userId) throws ValidationException{
        Article currArticle = articleRepository.find(articleId);
        if (currArticle == null) {
            throw new ValidationException("Can't find post");
        }
        long authorId = currArticle.getUserId();
        if (authorId != userId) {
            throw new ValidationException("You can only change visibility of your articles");
        }
    }
}
