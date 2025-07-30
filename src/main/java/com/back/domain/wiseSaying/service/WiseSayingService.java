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

    public int getWiseSayingCount() {
        return repository.getWiseSayingCount();
    }

    public List<WiseSaying> getSortedWiseSayings(int offset, int limit) {
        return repository.getWiseSayings(offset, limit);
    }

    public List<WiseSaying> getSortedWiseSayingsByKeyword(int offset, int limit, String keywordType, String keyword) {
        return repository.getWiseSayingsByKeyword(offset, limit, keywordType, keyword);
    }

    public int getTotalPages(int pageSize) {
        int count = repository.getWiseSayingCount();
        return (int) Math.ceil((double) count / pageSize);
    }

    public int getTotalPagesByKeyword(int pageSize, String keywordType, String keyword) {
        int count = repository.getWiseSayingCountByKeyword(keywordType, keyword);
        return (int) Math.ceil((double) count / pageSize);
    }

    public WiseSaying getWiseSayingById(int id) {
        return repository.getWiseSayingById(id);
    }

    public WiseSaying addWiseSaying(String content, String author) throws IOException {
        int lastId = repository.getLastId();
        WiseSaying wiseSaying = new WiseSaying(lastId, content, author);
        repository.add(wiseSaying);
        repository.saveWiseSayings(wiseSaying);
        repository.saveLastId();
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
