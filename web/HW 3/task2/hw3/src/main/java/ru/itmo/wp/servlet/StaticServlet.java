package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        String[] requests = uri.split("\\+");
        for (int i = 0; i < requests.length; i++) {
            if (!requests[i].startsWith("/")) {
                requests[i] = "/" + requests[i];
            }
        }

        if (!checkFileExistence(requests)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        boolean isFirstRequest = true;
        for (String currentUri : requests) {

            File file = new File(getServletContext().getRealPath("/static" + currentUri));
            if (isFirstRequest) {
                response.setContentType(getServletContext().getMimeType(file.getName()));
            }
            try (OutputStream outputStream = response.getOutputStream()) {
                Files.copy(file.toPath(), outputStream);
            }
            isFirstRequest = false;
        }
    }

    boolean checkFileExistence(String[] requests) {
        for (String currentUri : requests) {
            File file = new File(getServletContext().getRealPath("/static" + currentUri));
            if (!file.isFile()) {
                return false;
            }
        }
        return true;
    }
}
