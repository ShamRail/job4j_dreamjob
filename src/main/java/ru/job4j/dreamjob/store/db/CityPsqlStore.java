package ru.job4j.dreamjob.store.db;

import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.store.Store;
import ru.job4j.dreamjob.utils.ResourceScriptReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

public class CityPsqlStore implements Store<City> {

    private static final ConnectionPool POOL = ConnectionPool.getPool();

    private static final Store<City> STORE = new CityPsqlStore();

    private CityPsqlStore() {
        initTable();
    }

    public static Store<City> getStore() {
        return STORE;
    }

    private void initTable() {
        try (Connection connection = POOL.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(ResourceScriptReader.read("init_city.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<City> findAll() {
        Collection<City> cities = new LinkedList<>();
        try (Connection connection = POOL.getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from city;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    City city = new City(
                            rs.getInt("id"),
                            rs.getString("name")
                    );
                    cities.add(city);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public City findById(int id) {
        City city = new City();
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("select * from city where id = ?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    city = new City(
                            rs.getInt("id"),
                            rs.getString("name")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }

    @Override
    public City saveOrUpdate(City model) {
        return model.getId() == 0 ? save(model) : update(model);
    }

    public City save(City model) {
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement("insert into city(name) values(?);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public City update(City city) {
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("update city set name = ? where id = ?;")) {
            ps.setString(1, city.getName());
            ps.setInt(2, city.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("delete from city where id = ?;")) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
