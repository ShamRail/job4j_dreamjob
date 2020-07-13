package ru.job4j.dreamjob.controller;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {

    private static final Store store = Store.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String edit = req.getParameter("edit");
        String path = edit != null ? "/candidate/edit.jsp" : "/candidate/candidates.jsp";
        if (edit == null) {
            req.setAttribute("candidates", store.findAllCandidates());
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        store.saveCandidate(new Candidate(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("name"),
                req.getParameter("memo")
        ));
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }

}
