package ru.job4j.dreamjob.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Photo;
import ru.job4j.dreamjob.store.Store;
import ru.job4j.dreamjob.store.db.CandidatePsqlStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@MultipartConfig
public class CandidateServlet extends HttpServlet {

    private static final Store<Candidate> store = CandidatePsqlStore.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String edit = req.getParameter("edit");
        String path = edit != null ? "/candidate/edit.jsp" : "/candidate/candidates.jsp";
        if (edit == null) {
            req.setAttribute("candidates", store.findAll());
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File("./"));
        ServletFileUpload upload = new ServletFileUpload(factory);
        Candidate candidate = new Candidate();
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    if (!item.getName().isEmpty()) {
                        String name = UUID.randomUUID() + item.getName();
                        String path = folder + File.separator + name;
                        File file = new File(path);
                        try (FileOutputStream out = new FileOutputStream(file)) {
                            out.write(item.getInputStream().readAllBytes());
                        }
                        Photo photo = new Photo(name, path);
                        candidate.setPhoto(photo);
                    }
                } else {
                    String name = item.getFieldName();
                    if ("name".equals(name)) {
                        candidate.setName(item.getString());
                    } else if ("memo".equals(name)) {
                        candidate.setMemo(item.getString());
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        if (req.getParameter("id") != null) {
            candidate.setId(Integer.parseInt(req.getParameter("id")));
        }
        store.saveOrUpdate(candidate);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }

}
