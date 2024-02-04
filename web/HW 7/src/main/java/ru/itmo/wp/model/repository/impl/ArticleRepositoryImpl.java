package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.ArticleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepositoryImpl  extends AbstractRepositoryImpl implements ArticleRepository {
    @Override
    public void save(Article article) {
        super.save(
            article,
            saveQueryFabric("Article", "userId", "title", "text", "visibility", "creationTime"),
            article.getUserId(), article.getTitle(), article.getText(), article.isHidden(), article.getCreationTime()
        );
    }

    @Override
    public Article find(long id) {
        return super.find(id, "Article", ArticleRepositoryImpl::toArticle);
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Article` WHERE visibility=0 ORDER BY `creationTime` DESC")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    Article article;
                    while ((article = toArticle(statement.getMetaData(), resultSet)) != null) {
                        articles.add(article);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Articles.", e);
        }
        return articles;
    }

    @Override
    public List<Article> findUserArticles(long id) {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Article` WHERE userId=? ORDER BY `creationTime` DESC")) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    Article article;
                    while ((article = toArticle(statement.getMetaData(), resultSet)) != null) {
                        articles.add(article);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Articles.", e);
        }
        return articles;
    }

    @Override
    public void changeVisibility(long id, boolean visibility) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE `Article` SET visibility=? WHERE id=? ")) {
                statement.setBoolean(1, visibility);
                statement.setLong(2, id);
                statement.executeQuery();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Articles.", e);
        }
    }

    public static Article toArticle(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Article article = new Article();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    article.setId(resultSet.getLong(i));
                    break;
                case "userId":
                    article.setUserId(resultSet.getLong(i));
                    break;
                case "title":
                    article.setTitle(resultSet.getString(i));
                    break;
                case "text":
                    article.setText(resultSet.getString(i));
                    break;
                case "creationTime":
                    article.setCreationTime(resultSet.getTimestamp(i));
                    break;
                case "visibility":
                    article.setHidden(resultSet.getBoolean(i));
                    break;
                default:
                    // No operations.
            }
        }

        return article;
    }
}
