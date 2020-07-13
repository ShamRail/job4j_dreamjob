package ru.job4j.dreamjob.controller;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.memory.PostMemStore;
import ru.job4j.dreamjob.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostServlet extends HttpServlet {

    private final Store<Post> store = PostMemStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String edit = req.getParameter("edit");
        String path = edit != null ? "/post/edit.jsp" : "/post/posts.jsp";
        if (edit == null) {
            req.setAttribute("posts", PostMemStore.instOf().findAll());
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        Post post = new Post(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("name"),
                req.getParameter("description")
        );
        store.saveOrUpdate(post);
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }
}
