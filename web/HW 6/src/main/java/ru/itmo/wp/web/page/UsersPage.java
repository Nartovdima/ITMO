package ru.itmo.wp.web.page;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused"})
public class UsersPage extends Page {

    public void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
    }
}
