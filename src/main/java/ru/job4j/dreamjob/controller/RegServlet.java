package ru.job4j.dreamjob.controller;

import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.Store;
import ru.job4j.dreamjob.store.db.UserPsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    private static final Store<User> STORE = UserPsqlStore.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String exit = req.getParameter("exit");
        if (exit != null) {
            req.setAttribute("user", null);
            resp.sendRedirect(req.getContextPath());
        }
        req.setAttribute("error", null);
        req.getRequestDispatcher("/auth/reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") != null) {
            session.setAttribute("error", "Для регистрации нового пользователя необходимо выйти");
            req.getRequestDispatcher("/auth/reg.jsp").forward(req, resp);
        }
        User check = ((UserPsqlStore) STORE).findByEmailAndPassword(
                req.getParameter("email"),
                req.getParameter("password")
        );
        if (!check.getName().isEmpty()) {
            session.setAttribute("error", "Пользователь с этими данными уже существует");
            req.getRequestDispatcher("/auth/reg.jsp").forward(req, resp);
        }
        User user = new User();
        user.setName(req.getParameter("name"));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));
        STORE.saveOrUpdate(user);
        session.setAttribute("user", user);
        resp.sendRedirect(req.getContextPath());
    }
}
