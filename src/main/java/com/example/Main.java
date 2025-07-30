package com.example;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WiseSayingsManager manager = new WiseSayingsManager();
        manager.load();
        System.out.println("== 명언 앱 ==");
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("명령) ");
            String cmd = sc.nextLine();
            if (cmd.equals("종료")){
                manager.save();
                break;
            }
            else if (cmd.equals("등록")) manager.add(sc);
            else if (cmd.equals("목록")) manager.showAll();
            else if (cmd.equals("빌드")) manager.build();
            else if (cmd.contains("삭제") || cmd.contains("수정")) {
                String startCmd = cmd.substring(0, 2).trim();
                if (!startCmd.equals("삭제") && !startCmd.equals("수정")) continue;

                try{
                    int idToProcess = Integer.parseInt(getQueryParamsByCommand(cmd).get("id"));

                    if (startCmd.equals("수정")) manager.update(idToProcess, sc);
                    else if (startCmd.equals("삭제")) manager.remove(idToProcess);
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 id 형식입니다. 숫자를 입력해주세요.");
                }
            }
        }

        sc.close();
    }

    public static HashMap<String, String> getQueryParamsByCommand(String cmd) {
        HashMap<String, String> queryParams = new HashMap<>();
        int indexOfQuestionMark = cmd.indexOf("?");
        if (indexOfQuestionMark == -1) return queryParams; // No query parameters found

        String query = cmd.substring(indexOfQuestionMark + 1);
        if (query.isEmpty()) return queryParams; // No query parameters found

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }
}

class WiseSayingsManager {
    private static int lastId = 1;
    private List<WiseSaying> wiseSayings = new ArrayList<>();

    public WiseSaying getWiseSayingById(int id) {
        return wiseSayings.stream()
                .filter(ws -> ws.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<WiseSaying> getWiseSayings() {
        return wiseSayings.stream().sorted((a,b)->b.getId()-a.getId()).toList();
    }

    public void add(Scanner sc) {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        wiseSayings.add(new WiseSaying(lastId, content, author));
        System.out.println("%d번 명언이 등록되었습니다.".formatted(lastId));
        lastId++;
    }

    public void showAll() {
        if (wiseSayings.isEmpty()) {
            System.out.println("등록된 명언이 없습니다.");
            return;
        }
        System.out.println("번호 / 작가 / 명언");
        System.out.println("-------------------------");
        for (WiseSaying wiseSaying : getWiseSayings()) {
            System.out.println("%d / %s / %s".formatted(wiseSaying.getId(), wiseSaying.getContent(), wiseSaying.getAuthor()));
        }
    }

    public void remove(int id) {
        WiseSaying ws = getWiseSayingById(id);

        if (ws == null) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        wiseSayings.remove(ws);
        System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
    }

    public void update(int id, Scanner sc){
        WiseSaying ws = getWiseSayingById(id);

        if (ws == null) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        System.out.println("명언(기존) : %s".formatted(ws.getContent()));
        System.out.print("명언: ");
        String content = sc.nextLine();
        System.out.println("작가(기존) : %s".formatted(ws.getAuthor()));
        System.out.print("작가: ");
        String author = sc.nextLine();

        ws.setContent(content);
        ws.setAuthor(author);
        System.out.println("%d번 명언이 수정되었습니다.".formatted(id));
    }

    public void save() {
        String dirPath = "./db/wiseSaying";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (WiseSaying wiseSaying : wiseSayings) {
            File file = new File(dirPath, wiseSaying.getId() + ".json");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("{\n");
                writer.write("  \"id\": " + wiseSaying.getId() + ",\n");
                writer.write("  \"content\": \"" + wiseSaying.getContent() + "\",\n");
                writer.write("  \"author\": \"" + wiseSaying.getAuthor() + "\"\n");
                writer.write("}");
            } catch (Exception e) {
                System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
            }
        }

        File lastIdfile = new File(dirPath, "lastId.txt");
        try (FileWriter writer = new FileWriter(lastIdfile)) {
            writer.write(String.valueOf(lastId));
        } catch (Exception e) {
            System.out.println("lastId 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void load() {
        String dirPath = "./db/wiseSaying";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return; // No data to load
        }

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
                    wiseSayings.add(new WiseSaying(id, contentText, author));
                    lastId = Math.max(lastId, id + 1);
                } catch (Exception e) {
                    System.out.println("파일 로드 중 오류가 발생했습니다: " + e.getMessage());
                }
            }
        }

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

    public void build() {
        String dirPath = "./db";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dirPath,  "data.json");
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
        } catch (Exception e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }

        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}

class WiseSaying {
    private String content;
    private String author;
    private int id;

    WiseSaying(int id, String content, String author){
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public int getId() { return id;}
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public void setContent(String content) { this.content = content; }
    public void setAuthor(String author) { this.author = author; }
}