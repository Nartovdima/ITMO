package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {
    ArticleService articleService = new ArticleService();
    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.getSession().setAttribute("message", "For viewing this page you must be logged");
            throw new RedirectException("/index");
        }
    }
    public void makeArticle(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = (User) request.getSession().getAttribute("user");
        String title = request.getParameter("title");
        String text = request.getParameter("text");

        Article article = new Article();
        article.setUserId(user.getId());
        article.setTitle(title);
        article.setText(text);
        article.setHidden(false);
        articleService.makeRecord(article);
        request.getSession().setAttribute("message", "Success!");

        throw new RedirectException("/article");
    }
}
