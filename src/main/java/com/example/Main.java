package com.example;

import java.util.ArrayList;
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
        }

        sc.close();
    }
}

class WiseSayingsManager {
    private static int id = 1;
    private List<WiseSaying> wiseSayings = new ArrayList<>();

    public List<WiseSaying> getWiseSayings() {
        return wiseSayings.stream().sorted((a,b)->b.getId()-a.getId()).toList();
    }

    public void add(Scanner sc) {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        wiseSayings.add(new WiseSaying(id, content, author));
        System.out.println("%d번 명언이 등록되었습니다.".formatted(id));
        id++;
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