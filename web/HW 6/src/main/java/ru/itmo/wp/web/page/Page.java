package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.EventService;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.model.service.UserService;
import java.util.Map;

public abstract class Page {
    protected final UserService userService = new UserService();
    protected final EventService eventService = new EventService();
    protected final TalkService talkService = new TalkService();
    protected HttpServletRequest currRequest;
    public void action(HttpServletRequest request, Map<String, Object> view) {
        //No op
    }

    public void before(HttpServletRequest request, Map<String, Object> view) {
        currRequest = request;
        setUser(view);
        setMessage(request, view);
    }

    public void after(HttpServletRequest request, Map<String, Object> view) {
        view.put("userCount", userService.findCount());
    }

    protected void setUser(Map<String, Object> view) {
        User user = (User) currRequest.getSession().getAttribute("user");
        if (user != null) {
            view.put("user", user);
        }
    }

    protected User getUser() {
        return (User) currRequest.getSession().getAttribute("user");
    }

    private void setMessage(HttpServletRequest request, Map<String, Object> view) {
        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }
}
