package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatServlet extends HttpServlet {
    private static final String AUTH_REQ = "/message/auth";
    private static final String FINDALL_REQ = "/message/findAll";
    private static final String ADD_REQ = "/message/add";
    private static final String NAME_PARAM = "user";
    private static class Message {
        final public String user;
        final public String text;
        public Message(String user, String text) {
            this.user = user;
            this.text = text;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "user='" + user + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    private final List<Message> messsages = new ArrayList<>();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        switch (uri) {
            case AUTH_REQ:
                processAuth(request, response);
                break;
            case FINDALL_REQ:
                viewMessages(request, response);
                break;
            case ADD_REQ:
                sendMessage(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
        }
        response.setContentType("application/json");
    }

    private static void processAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object userName = request.getParameter(NAME_PARAM);
        if (userName != null) {
            request.getSession().setAttribute(NAME_PARAM, userName.toString());
        } else {
            userName = request.getSession().getAttribute(NAME_PARAM);
        }

        if (userName == null) {
            userName = "";
        }

        String responseJson = new Gson().toJson(userName.toString());
        response.getWriter().print(responseJson);
        response.getWriter().flush();
    }

    private void viewMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String responseJson = new Gson().toJson(messsages);
        response.getWriter().print(responseJson);
        response.getWriter().flush();
    }

    private void sendMessage(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getSession().getAttribute(NAME_PARAM).toString();
        String message = request.getParameter("text");
        messsages.add(new Message(userName, message));
    }
}
