package ru.job4j.dreamjob.controller;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.Store;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {

    private static final Store store = Store.instOf();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        store.saveCandidate(new Candidate(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("name"),
                req.getParameter("memo")
        ));
        resp.sendRedirect(req.getContextPath() + "/candidate/candidates.jsp");
    }

}
