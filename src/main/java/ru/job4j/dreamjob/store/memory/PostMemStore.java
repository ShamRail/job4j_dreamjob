package ru.job4j.dreamjob.store.memory;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.Store;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostMemStore implements Store<Post> {

    private static final AtomicInteger POST_ID = new AtomicInteger(1);

    private static final Store<Post> INST = new PostMemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostMemStore() {
        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Junior Java Job", "Some description ..."));
        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Middle Java Job", "Some description ..."));
        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Senior Java Job", "Some description ..."));
    }

    public static Store<Post> instOf() {
        return INST;
    }

    @Override
    public Collection<Post> findAll() {
        return posts.values();
    }

    @Override
    public Post findById(int id) {
        return posts.get(id);
    }

    @Override
    public Post saveOrUpdate(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
        return post;
    }

    @Override
    public boolean delete(int id) {
        return posts.remove(id) != null;
    }
}
