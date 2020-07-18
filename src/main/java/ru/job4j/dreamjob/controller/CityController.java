package ru.job4j.dreamjob.controller;

import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.store.Store;
import ru.job4j.dreamjob.store.db.CityPsqlStore;
import ru.job4j.dreamjob.utils.JSONUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class CityController extends HttpServlet {

    private static final Store<City> STORE = CityPsqlStore.getStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Collection<City> cities = STORE.findAll();
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        String json = JSONUtils.serialize(cities);
        writer.println(json);
        writer.flush();
        writer.close();
    }

}
