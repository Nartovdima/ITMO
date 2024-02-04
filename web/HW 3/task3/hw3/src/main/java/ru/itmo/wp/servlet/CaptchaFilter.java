package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Object access = request.getSession().getAttribute("access");

        if (access == null) {
            Object code = request.getParameter("code");
            Object answer = request.getSession().getAttribute("answer");

            if (answer != null && answer.equals(code)) {
                request.getSession().setAttribute("access", "true");
            } else if (code != null || answer == null) {
                int newCode = new Random().nextInt(900) + 100;
                String img = new String(Base64.getEncoder().encode(ImageUtils.toPng(String.valueOf(newCode))));
                request.getSession().setAttribute("answer", String.valueOf(newCode));

                response.setContentType("text/html");
                response.getOutputStream().print(
                        "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<div>" +
                        "   <img src=\"data:image/png;base64," + img + "\" alt=\"number\" />" +
                        "</div>\n" +
                        "<div class=\"captcha-input-field\">\n" +
                        "    <form method=\"post\">\n" +
                        "        <label for=\"captcha-code\">Enter code:</label>\n" +
                        "        <input name=\"code\" id=\"captcha-code\">\n" +
                        "    </form>\n" +
                        "</div>");


            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
