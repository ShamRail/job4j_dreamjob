package ru.job4j.dreamjob.store.db;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.Store;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class PostPsqlStore implements Store<Post> {

    private static final ConnectionPool POOL = ConnectionPool.getPool();

    private static final PostPsqlStore STORE = new PostPsqlStore();

    private PostPsqlStore() {
        initTable();
    }

    public static Store<Post> getStore() {
        return STORE;
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

    @Override
    public Collection<Post> findAll() {
        Collection<Post> posts = new LinkedList<>();
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement("select * from post;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = new Post(0, "", "");
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement("select * from post where id = ?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = new Post(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Post saveOrUpdate(Post model) {
        return model.getId() == 0 ? save(model) : update(model);
    }

    private Post update(Post model) {
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement(
                "update post set name = ?, description = ? where id = ?;", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getDescription());
            ps.setInt(3, model.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private Post save(Post model) {
        try (Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement(
                "insert into post(name, description) values(?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, model.getName());
            ps.setString(2, model.getDescription());
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
                PreparedStatement ps = connection.prepareStatement("delete from post where id = ?;")) {
            ps.setInt(1, id);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Connection connect() throws SQLException {
        return POOL.getConnection();
    }

    public static void main(String[] args) {
        Store<Post> store = PostPsqlStore.getStore();

        Post post1 = new Post("name1", "desc1");
        Post post2 = new Post("name2", "desc2");
        Post post3 = new Post("name3", "desc3");

        store.saveOrUpdate(post1);
        store.saveOrUpdate(post2);
        store.saveOrUpdate(post3);

        System.out.println("======== FIND ALL ========");
        store.findAll().forEach(System.out::println);
        System.out.println();

        System.out.println("======== FIND BY ID ========");
        System.out.println(store.findById(post3.getId()));
        System.out.println();

        System.out.println("======== UPDATE ========");
        post3.setName("upd");
        store.saveOrUpdate(post3);
        System.out.println(store.findById(post3.getId()));
        System.out.println();

        System.out.println("======== DELETE ========");
        store.delete(post3.getId());
        store.findAll().forEach(System.out::println);

    }

}
