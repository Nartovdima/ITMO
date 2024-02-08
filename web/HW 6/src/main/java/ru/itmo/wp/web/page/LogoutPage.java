package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused"})
public class LogoutPage extends Page {
    @Override
    public void action(HttpServletRequest request, Map<String, Object> view) {

        Event event = new Event();
        event.setUserId(getUser().getId());
        event.setType(Event.Type.LOGOUT);
        eventService.makeRecord(event);

        request.getSession().removeAttribute("user");
        request.getSession().setAttribute("message", "Good bye. Hope to see you soon!");

        throw new RedirectException("/index");
    }
}
