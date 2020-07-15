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

public class AuthServlet extends HttpServlet {

    private static final Store<User> STORE = UserPsqlStore.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", null);
        req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sc = req.getSession();
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = ((UserPsqlStore) STORE).findByEmailAndPassword(email, password);
        if (user.getName().isEmpty()) {
            req.setAttribute("error", "Не верный email или пароль");
            req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
        } else {
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        }
    }
}
