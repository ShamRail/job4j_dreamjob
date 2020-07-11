package ru.job4j.dreamjob.controller;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.Store;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.time.LocalDateTime.now;

public class PostServlet extends HttpServlet {

    private final Store store = Store.instOf();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        store.savePost(new Post(
                req.getParameter("name"),
                req.getParameter("description"),
                now()
        ));
        resp.sendRedirect(req.getContextPath() + "/post/posts.jsp");
    }
}
