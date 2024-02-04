package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TalksPage extends Page {
    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        List<User> users = userService.findAll();
        Map<String, String> userById = new HashMap<>();
        for (User currUser : users) {
            userById.put(String.valueOf(currUser.getId()), currUser.getLogin());
        }
        view.put("users", users);
        view.put("userById", userById);
        view.put("talk", talkService.findMessages(getUser()));
    }

    @Override
    public void action(HttpServletRequest request, Map<String, Object> view) {
        if (getUser() == null) {
            request.getSession().setAttribute("message", "For viewing this page you must be logged");
            throw new RedirectException("/index");
        }
    }

    public void sendMessage(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        long targetUserId = Long.parseLong(request.getParameter("targetUser"));
        String text = request.getParameter("text");
        text = text.trim();
        Talk talk = new Talk();
        talk.setSourceUserId(getUser().getId());
        talk.setTargetUserId(targetUserId);
        talk.setText(text);
        talkService.validateText(talk);
        talkService.makeRecord(talk);
        throw new RedirectException("/talks");
    }
}
