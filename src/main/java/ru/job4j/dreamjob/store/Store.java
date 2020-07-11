package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {

    private static final AtomicInteger POST_ID = new AtomicInteger(1);

    private static final Store INST = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Store() {

        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Junior Java Job", "Some description ...", LocalDateTime.now().minusDays(1)));
        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Middle Java Job", "Some description ...", LocalDateTime.now().minusDays(10)));
        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Senior Java Job", "Some description ...", LocalDateTime.now().minusDays(15)));

        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public void savePost(Post post) {
        post.setId(POST_ID.incrementAndGet());
        posts.put(POST_ID.get(), post);
    }

}
