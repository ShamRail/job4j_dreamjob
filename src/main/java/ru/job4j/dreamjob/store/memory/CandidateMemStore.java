package ru.job4j.dreamjob.store.memory;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.Store;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateMemStore implements Store<Candidate> {

    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(1);

    private static final Store<Candidate> INST = new CandidateMemStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private CandidateMemStore() {
        candidates.put(CANDIDATE_ID.incrementAndGet(), new Candidate(CANDIDATE_ID.get(), "Junior Java", "memo"));
        candidates.put(CANDIDATE_ID.incrementAndGet(), new Candidate(CANDIDATE_ID.get(), "Middle Java", "memo"));
        candidates.put(CANDIDATE_ID.incrementAndGet(), new Candidate(CANDIDATE_ID.get(), "Senior Java", "memo"));
    }

    public static Store<Candidate> instOf() {
        return INST;
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    @Override
    public Candidate findById(int id) {
        return candidates.get(id);
    }

    @Override
    public Candidate saveOrUpdate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean delete(int id) {
        return candidates.remove(id) != null;
    }
}
