package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/** @noinspection unused*/
public class UsersPage {
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            view.put("user", userService.find(user.getId()));
        }
    }

    private void findAll(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
    }

    private void findUser(HttpServletRequest request, Map<String, Object> view) {
        view.put("user",
                userService.find(Long.parseLong(request.getParameter("userId"))));
    }

    private void changeAdminStatus(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = (User) request.getSession().getAttribute("user");
        long operatedUserId = Long.parseLong(request.getParameter("operatedUserId"));
        boolean status = "disable".equals(request.getParameter("status"));
        boolean success = true;
        try {
            userService.changeAdminStatus(user, operatedUserId, status);
        } catch (ValidationException e) {
            success = false;
            view.put("error", e);
        }
        view.put("success", success);
    }
}
