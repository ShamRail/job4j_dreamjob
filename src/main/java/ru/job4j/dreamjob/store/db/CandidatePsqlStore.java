package ru.job4j.dreamjob.store.db;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Photo;
import ru.job4j.dreamjob.store.Store;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class CandidatePsqlStore implements Store<Candidate> {

    private static final ConnectionPool POOL = ConnectionPool.getPool();

    private static final CandidatePsqlStore STORE = new CandidatePsqlStore();

    private static final Store<Photo> PHOTO_STORE = PhotoPsqlStore.getStore();

    private CandidatePsqlStore() {
        initTable();
    }

    private void initTable() {
        try (Connection connection = connect();
                Statement stmt = connection.createStatement()) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(getClass().getClassLoader().getResourceAsStream("schema.sql")))) {
                String text = br.lines().collect(Collectors.joining(System.lineSeparator()));
                stmt.execute(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Store<Candidate> getStore() {
        return STORE;
    }

    @Override
    public Collection<Candidate> findAll() {
        Collection<Candidate> posts = new LinkedList<>();
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement("select * from candidate;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Candidate candidate = new Candidate(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("memo")
                    );
                    posts.add(candidate);
                    Integer photoId = rs.getObject("photo_id", Integer.class);
                    if (photoId != null) {
                        candidate.setPhoto(PHOTO_STORE.findById(photoId));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Candidate findById(int id) {
        Candidate candidate = new Candidate(0, "", "");
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement("select * from candidate where id = ?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate = new Candidate(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("memo")
                    );
                    Integer photoId = rs.getObject("photo_id", Integer.class);
                    if (photoId != null) {
                        candidate.setPhoto(PHOTO_STORE.findById(photoId));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    @Override
    public Candidate saveOrUpdate(Candidate model) {
        return model.getId() == 0 ? save(model) : update(model);
    }

    private Candidate update(Candidate model) {
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement(
                "update candidate set name = ?, memo = ?, photo_id = ? where id = ?;", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getMemo());
            ps.setInt(4, model.getId());
            if (model.getPhoto() != null) {
                PHOTO_STORE.saveOrUpdate(model.getPhoto());
                ps.setInt(3, model.getPhoto().getId());
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private Candidate save(Candidate model) {
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement(
                "insert into candidate(name, memo, photo_id) values(?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getMemo());
            ps.setNull(3, Types.INTEGER);
            if (model.getPhoto() != null) {
                Photo photo = PHOTO_STORE.saveOrUpdate(model.getPhoto());
                ps.setInt(3, photo.getId());
            }
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                model.setId(rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement("delete from candidate where id = ?;")) {
            Candidate candidate = findById(id);
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
            if (candidate.getPhoto() != null) {
                PHOTO_STORE.delete(candidate.getPhoto().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Connection connect() throws SQLException {
        return POOL.getConnection();
    }
}
