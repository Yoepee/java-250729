package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WiseSayingRepository {
    private static int lastId = 1;
    private List<WiseSaying> wiseSayings = new ArrayList<>();

    public int getLastId() {
        return lastId;
    }

    public WiseSaying getWiseSayingById(int id) {
        return wiseSayings.stream()
                .filter(ws -> ws.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<WiseSaying> getWiseSayings(int offset, int limit) {
        return wiseSayings.stream()
                .sorted((a, b) -> b.getId() - a.getId())
                .skip(offset)
                .limit(limit)
                .toList();
    }
    public int getWiseSayingCount() {
        return wiseSayings.size();
    }

    public List<WiseSaying> getWiseSayingsByKeyword(int offset, int limit, String keywordType, String keyword) {
        return wiseSayings.stream()
                .filter(ws -> keywordType.equals("content") ? ws.getContent().toLowerCase().contains(keyword.toLowerCase()) : ws.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                .sorted((a, b) -> b.getId() - a.getId())
                .skip(offset)
                .limit(limit)
                .toList();
    }

    public int getWiseSayingCountByKeyword(String keywordType, String keyword) {
        return (int)wiseSayings.stream()
                .filter(ws -> keywordType.equals("content") ? ws.getContent().toLowerCase().contains(keyword.toLowerCase()) : ws.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                .count();
    }

    public void add(WiseSaying ws) {
        wiseSayings.add(ws);
        lastId++;
    }

    public void remove(WiseSaying ws) {
        wiseSayings.remove(ws);
    }

    public void update(WiseSaying ws, String content, String author) {
        ws.setContent(content);
        ws.setAuthor(author);
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

    public void saveWiseSayings(WiseSaying ws) throws IOException {
        String dirPath = "./db/wiseSaying";
        getDir(dirPath);
        File file = new File(dirPath, ws.getId() + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{\n");
            writer.write("  \"id\": " + ws.getId() + ",\n");
            writer.write("  \"content\": \"" + ws.getContent() + "\",\n");
            writer.write("  \"author\": \"" + ws.getAuthor() + "\"\n");
            writer.write("}");
        }
    }

    public void saveLastId() throws IOException {
        String dirPath = "./db/wiseSaying";
        getDir(dirPath);
        File lastIdfile = new File(dirPath, "lastId.txt");

        try (FileWriter writer = new FileWriter(lastIdfile)) {
            writer.write(String.valueOf(lastId));
        }
    }

    public void build() throws IOException {
        String dirPath = "./db";
        getDir(dirPath);

        File file = new File(dirPath, "data.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("[\n");
            for (WiseSaying wiseSaying : wiseSayings) {
                writer.write("  {\n");
                writer.write("    \"id\": " + wiseSaying.getId() + ",\n");
                writer.write("    \"content\": \"" + wiseSaying.getContent() + "\",\n");
                writer.write("    \"author\": \"" + wiseSaying.getAuthor() + "\"\n");
                writer.write("  }");

                if (wiseSaying != wiseSayings.get(wiseSayings.size() - 1)) {
                    writer.write(",\n");
                } else {
                    writer.write("\n");
                }
            }
            writer.write("]");
        }
    }

    public void loadWiseSayings() throws IOException {
        String dirPath = "./db/wiseSaying";
        File dir = getDir(dirPath);

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
                } catch (Exception e) {
                    System.out.println("파일 로드 중 오류가 발생했습니다: " + e.getMessage());
                }
            }
        }
    }

    public void loadLastId() throws IOException {
        String dirPath = "./db/wiseSaying";
        getDir(dirPath);

        File lastIdfile = new File(dirPath, "lastId.txt");
        if (lastIdfile.exists()) {
            try (Scanner scanner = new Scanner(lastIdfile)) {
                if (scanner.hasNextLine()) {
                    lastId = Integer.parseInt(scanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("lastId 로드 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
    }
}
