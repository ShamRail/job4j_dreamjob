package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {

    private static final AtomicInteger POST_ID = new AtomicInteger(1);

    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(1);

    private static final Store INST = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Store() {

        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Junior Java Job", "Some description ..."));
        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Middle Java Job", "Some description ..."));
        posts.put(POST_ID.incrementAndGet(), new Post(POST_ID.get(), "Senior Java Job", "Some description ..."));

        candidates.put(CANDIDATE_ID.incrementAndGet(), new Candidate(CANDIDATE_ID.get(), "Junior Java", "memo"));
        candidates.put(CANDIDATE_ID.incrementAndGet(), new Candidate(CANDIDATE_ID.get(), "Middle Java", "memo"));
        candidates.put(CANDIDATE_ID.incrementAndGet(), new Candidate(CANDIDATE_ID.get(), "Senior Java", "memo"));
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
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findPostById(int id) {
        return posts.get(id);
    }

    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }


    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }
}
