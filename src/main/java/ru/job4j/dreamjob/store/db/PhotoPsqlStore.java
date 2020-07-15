package ru.job4j.dreamjob.store.db;

import ru.job4j.dreamjob.model.Photo;
import ru.job4j.dreamjob.store.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

public class PhotoPsqlStore implements Store<Photo> {

    private static final ConnectionPool POOL = ConnectionPool.getPool();

    private static final PhotoPsqlStore STORE = new PhotoPsqlStore();

    public static Store<Photo> getStore() {
        return STORE;
    }

    private PhotoPsqlStore() { }

    @Override
    public Collection<Photo> findAll() {
        Collection<Photo> photos = new LinkedList<>();
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("select * from photo;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    photos.add(new Photo(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("path")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photos;
    }

    @Override
    public Photo findById(int id) {
        Photo photo = new Photo();
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("select * from photo where id = ?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    photo = new Photo(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("path")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photo;
    }

    @Override
    public Photo saveOrUpdate(Photo model) {
        return model.getId() == 0 ? save(model) : update(model);
    }

    private Photo update(Photo model) {
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("update photo set name = ?, path = ? where id = ?;")) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getPath());
            ps.setInt(3, model.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private Photo save(Photo model) {
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("insert into photo(name, path) values(?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getPath());
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

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (Connection connection = POOL.getConnection();
             PreparedStatement ps = connection.prepareStatement("delete from photo where id = ?;")) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
