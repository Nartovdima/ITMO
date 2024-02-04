package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.service.UserService;

@Controller
public class UserPage extends Page {

    private final UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value={"/user/{id}", "/user"})
    public String getUserPage(@PathVariable(required = false) String id, Model model) {
        User user;
        try{
            user = userService.findById(Long.parseLong(id));
        } catch (NumberFormatException ignored) {
            user = null;
        }
        model.addAttribute("user", user);
        return "UserPage";
    }
}
