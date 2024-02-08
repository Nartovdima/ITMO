package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

public class MyArticlesPage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.getSession().setAttribute("message", "For viewing this page you must be logged");
            throw new RedirectException("/index");
        }
        view.put("articles", articleService.findUserArticles(user.getId()));
    }

    private void changeVisibility(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = (User) request.getSession().getAttribute("user");
        long articleId = Long.parseLong(request.getParameter("articleId"));
        boolean articleVisibility = "hide".equals(request.getParameter("visibility"));
        boolean success = true;
        try{
            articleService.changeVisibility(articleId, articleVisibility, user);
        } catch (ValidationException e) {
            success = false;
            view.put("error", e.getMessage());
        }
        view.put("success", success);
    }
}
