package ru.job4j.dreamjob.store.db;

import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.Store;
import ru.job4j.dreamjob.utils.ResourceScriptReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

public class UserPsqlStore implements Store<User> {

    private static final ConnectionPool POOL = ConnectionPool.getPool();

    private static final Store<User> STORE = new UserPsqlStore();

    public static Store<User> getStore() {
        return STORE;
    }

    private UserPsqlStore() {
        this.initTable();
    }

    private void initTable() {
        try (Connection connection = POOL.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(ResourceScriptReader.read("init_user.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> users = new LinkedList<>();
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("select * from _user;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findById(int id) {
        User user = new User();
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("select * from _user where id = ?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User saveOrUpdate(User model) {
        return model.getId() == 0 ? save(model) : update(model);
    }

    private User save(User model) {
        try (Connection connection = POOL.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "insert into _user(name, email, password) values(?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getEmail());
            ps.setString(3, model.getPassword());
            ps.executeUpdate();
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

    private User update(User model) {
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "update _user set name = ?, email = ?, password = ?;", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getEmail());
            ps.setString(3, model.getPassword());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("delete from _user where id = ?;")) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
