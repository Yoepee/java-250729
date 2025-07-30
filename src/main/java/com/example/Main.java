package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WiseSayingsManager manager = new WiseSayingsManager();
        System.out.println("== 명언 앱 ==");
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("명령) ");
            String cmd = sc.nextLine();
            if (cmd.equals("종료")) break;
            else if (cmd.equals("등록")) manager.add(sc);
            else if (cmd.equals("목록")) manager.showAll();
            else if (cmd.contains("삭제") || cmd.contains("수정")) {
                try{
                    int idToProcess = Integer.parseInt(getQueryParamsByCommand(cmd).get("id"));

                    if(cmd.contains("수정"))  manager.update(idToProcess, sc);
                    else if (cmd.contains("삭제")) manager.remove(idToProcess);
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

        String query = cmd.substring(cmd.indexOf("?") + 1);
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
            System.out.println("%d / %s /%s".formatted(wiseSaying.getId(), wiseSaying.getContent(), wiseSaying.getAuthor()));
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