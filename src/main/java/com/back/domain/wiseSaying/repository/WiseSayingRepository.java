package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WiseSayingRepository {
    private static int lastId = 1;
    private List<WiseSaying> wiseSayings = new ArrayList<>();
    private static final String FILE_DIR_PATH = "./db/wiseSaying";
    private static final String BUILD_DIR_PATH = "./db";

    public WiseSaying getWiseSayingById(int id) {
        return wiseSayings.stream()
                .filter(ws -> ws.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<WiseSaying> getWiseSayings(int offset, int limit, String keywordType, String keyword) {
        boolean isSearch = keywordType != null && keyword != null;

        return wiseSayings.stream()
                .filter(ws -> {
                    if (!isSearch) return true;
                    String field = keywordType.equals("content") ? ws.getContent() : ws.getAuthor();
                    return field.toLowerCase().contains(keyword.toLowerCase());
                })
                .sorted((a, b) -> b.getId() - a.getId())
                .skip(offset)
                .limit(limit)
                .toList();
    }

    public int getWiseSayingCount(String keywordType, String keyword) {
        boolean isSearch = keywordType != null && keyword != null;

        return (int) wiseSayings.stream()
                .filter(ws -> {
                    if (!isSearch) return true;
                    String field = keywordType.equals("content") ? ws.getContent() : ws.getAuthor();
                    return field.toLowerCase().contains(keyword.toLowerCase());
                })
                .count();
    }

    public int getNextId() {
        return lastId++;
    }

    public void add(WiseSaying ws) {
        wiseSayings.add(ws);
    }

    public void remove(WiseSaying ws) {
        wiseSayings.remove(ws);
    }

    public void update(WiseSaying ws, String content, String author, LocalDateTime modifyDate) {
        ws.setContent(content);
        ws.setAuthor(author);
        ws.setModifyDate(modifyDate);
    }

    private File getDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    public boolean removeWiseSayingFile(int id) {
        String path = "./db/wiseSaying/" + id + ".json";
        File file = new File(path);

        if (!file.exists()) {
            return false; // 파일이 존재하지 않음
        }

        return file.delete(); // 삭제 성공 여부 반환
    }

    private String getWiseSayingToJsonString(WiseSaying ws, String startStr) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append(startStr+"{\n");
        jsonBuilder.append(startStr+"  \"id\": ").append(ws.getId()).append(",\n");
        jsonBuilder.append(startStr+"  \"content\": \"").append(ws.getContent().replace("\"", "\\\"")).append("\",\n");
        jsonBuilder.append(startStr+"  \"author\": \"").append(ws.getAuthor().replace("\"", "\\\"")).append("\"\n");
        jsonBuilder.append(startStr+"}");

        return jsonBuilder.toString();
    }

    public void saveWiseSayings(WiseSaying ws) throws IOException {
        getDir(FILE_DIR_PATH);
        File file = new File(FILE_DIR_PATH, ws.getId() + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(getWiseSayingToJsonString(ws, ""));
        }
    }

    public void saveLastId() throws IOException {
        getDir(FILE_DIR_PATH);
        File lastIdfile = new File(FILE_DIR_PATH, "lastId.txt");

        try (FileWriter writer = new FileWriter(lastIdfile)) {
            writer.write(String.valueOf(lastId));
        }
    }

    public void build() throws IOException {
        getDir(BUILD_DIR_PATH);

        File file = new File(BUILD_DIR_PATH, "data.json");
        try (FileWriter writer = new FileWriter(file)) {
            StringBuilder content = new StringBuilder();
            content.append("[\n");
            for (WiseSaying wiseSaying : wiseSayings) {
                content.append(getWiseSayingToJsonString(wiseSaying, "  "));
                content.append(wiseSaying != wiseSayings.get(wiseSayings.size() - 1) ? ",\n" : "\n");
            }
            content.append("]");
            writer.write(content.toString());
        }
    }

    public void loadWiseSayings() throws IOException {
        File dir = getDir(FILE_DIR_PATH);

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try (Scanner scanner = new Scanner(file)) {
                    StringBuilder content = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        content.append(scanner.nextLine()).append("\n");
                    }
                    String json = content.toString();
                    int id = Integer.parseInt(json.split("\"id\": ")[1].split(",")[0]);
                    String contentText = json.split("\"content\": \"")[1].split("\"")[0];
                    String author = json.split("\"author\": \"")[1].split("\"")[0];
                    add(new WiseSaying(id, contentText, author));
                    lastId = Math.max(lastId, id + 1);
                }
            }
        }
    }

    public void loadLastId() throws IOException {
        getDir(FILE_DIR_PATH);

        File lastIdfile = new File(FILE_DIR_PATH, "lastId.txt");
        if (lastIdfile.exists()) {
            try (Scanner scanner = new Scanner(lastIdfile)) {
                if (scanner.hasNextLine()) lastId = Integer.parseInt(scanner.nextLine());
            }
        }
    }
}
