package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.io.IOException;
import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository repository;

    public WiseSayingService() {
        this.repository = new WiseSayingRepository();
    }

    public int getWiseSayingCount(String keywordType, String keyword) {
        return repository.getWiseSayingCount(keywordType, keyword);
    }

    public int getTotalPages(int pageSize, String keywordType, String keyword) {
        int count = repository.getWiseSayingCount(keywordType, keyword);
        return (int) Math.ceil((double) count / pageSize);
    }

    public List<WiseSaying> getWiseSayings(int offset, int limit, String keywordType, String keyword) {
        return repository.getWiseSayings(offset, limit, keywordType, keyword);
    }

    public WiseSaying getWiseSayingById(int id) {
        return repository.getWiseSayingById(id);
    }

    public WiseSaying addWiseSaying(String content, String author) throws IOException {
        WiseSaying ws = new WiseSaying(0, content, author);
        repository.save(ws);
        repository.saveWiseSayings(ws);
        repository.saveLastId();
        return ws;
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
        ws.setContent(content);
        ws.setAuthor(author);
        repository.save(ws);
        repository.saveWiseSayings(ws);
        return "success";
    }

    public String build() throws IOException {
        repository.build();
        return "success";
    }

    public void load() throws IOException {
        repository.loadWiseSayings();
        repository.loadLastId();
    }
}
