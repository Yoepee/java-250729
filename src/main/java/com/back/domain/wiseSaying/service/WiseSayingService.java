package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.io.IOException;
import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository repository;

    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }

    public List<WiseSaying> getSortedWiseSayings() {
        return repository.getWiseSayings().stream().sorted((a, b) -> b.getId() - a.getId()).toList();
    }

    public WiseSaying getWiseSayingById(int id) {
        return repository.getWiseSayings().stream()
                .filter(ws -> ws.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean isEmpty() {
        return repository.getWiseSayings().isEmpty();
    }

    public WiseSaying addWiseSaying(String content, String author) throws IOException {
        int lastId = repository.getLastId();
        WiseSaying wiseSaying = new WiseSaying(lastId, content, author);
        repository.saveWiseSayings(wiseSaying);
        repository.saveLastId();
        repository.add(wiseSaying);
        return wiseSaying;
    }

    public String remove(WiseSaying ws) {
        boolean isFileRemoved = repository.removeWiseSayingFile(ws.getId());
        if (!isFileRemoved) {
            return "fail";
        }

        repository.remove(ws);
        return "success";
    }

    public String update(WiseSaying ws, String content, String author) throws IOException {
        repository.saveWiseSayings(ws);
        repository.update(ws, content, author);
        return "success";
    }

    public String build() throws IOException {
        repository.build();
        return "success";
    }

    public String load() throws IOException {
        repository.loadWiseSayings();
        repository.loadLastId();
        return "success";
    }
}
