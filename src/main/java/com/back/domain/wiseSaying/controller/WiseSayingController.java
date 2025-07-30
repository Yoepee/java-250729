package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final WiseSayingService service;

    public WiseSayingController(WiseSayingService service) {
        this.service = service;
    }

    public void add(Scanner sc) {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();
        try {
            WiseSaying ws = service.addWiseSaying(content, author);
            int id = ws.getId();
            System.out.println("%d번 명언이 등록되었습니다.".formatted(id));
        } catch (IOException e) {
            System.out.println("⚠파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void showAll(int pageSize, int page) {
        if (service.getWiseSayingCount() == 0) {
            System.out.println("등록된 명언이 없습니다.");
            return;
        }
        int totalPages = service.getTotalPages(pageSize);
        if (page < 1 || page > totalPages) {
            System.out.println("유효하지 않은 페이지입니다. (1 ~ %d)".formatted(totalPages));
            return;
        }
        System.out.println("번호 / 작가 / 명언");
        System.out.println("-------------------------");
        int offset = (page - 1) * pageSize;
        List<WiseSaying> sortedWiseSayings= service.getSortedWiseSayings(offset, pageSize);
        showList(sortedWiseSayings, totalPages, page);
    }

    public void showByKeyword(int pageSize, int page, String keywordType, String keyword) {
        if (service.getWiseSayingCount() == 0) {
            System.out.println("등록된 명언이 없습니다.");
            return;
        }
        int totalPages = service.getTotalPagesByKeyword(pageSize, keywordType, keyword);
        if (page < 1 || page > totalPages) {
            System.out.println("유효하지 않은 페이지입니다. (1 ~ %d)".formatted(totalPages));
            return;
        }

        System.out.println("-------------------------");
        System.out.println("검색 타입 : %s".formatted(keywordType));
        System.out.println("검색어 : %s".formatted(keyword));
        System.out.println("-------------------------");
        System.out.println("번호 / 작가 / 명언");
        System.out.println("-------------------------");
        int offset = (page - 1) * pageSize;
        List<WiseSaying> sortedWiseSayings= service.getSortedWiseSayingsByKeyword(offset, pageSize, keywordType, keyword);
        showList(sortedWiseSayings, totalPages, page);
    }
    public void showList(List<WiseSaying> wiseSayings, int totalPages, int page) {
        for (WiseSaying wiseSaying : wiseSayings) {
            System.out.println("%d / %s / %s".formatted(wiseSaying.getId(), wiseSaying.getContent(), wiseSaying.getAuthor()));
        }
        System.out.println("-------------------------");
        System.out.print("페이지 : ");
        for (int i = 1; i <= totalPages; i++) {
            if (i == page) {
                System.out.print("[%d] ".formatted(i));
            } else {
                System.out.print("%d ".formatted(i));
            }

            if (i != totalPages) {
                System.out.print("/ ");
            }
        }
        System.out.println();

    }

    public void remove(int id) {
        WiseSaying ws = service.getWiseSayingById(id);
        if (ws == null) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }
        String response = service.remove(ws);

        if (response.equals("success")) System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
        else System.out.println("파일 삭제 중 오류가 발생했습니다.");
    }

    public void update(int id, Scanner sc) {
        WiseSaying ws = service.getWiseSayingById(id);
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

        try {
            String response = service.update(ws, content, author);
            if (response.equals("success")) System.out.println("%d번 명언이 수정되었습니다.".formatted(id));
        } catch (IOException e) {
            System.out.println("⚠파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void build() {
        try {
            String response = service.build();
            if (response.equals("success")) System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch (Exception e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void load() {
        try {
            service.load();
        } catch (Exception e) {
            System.out.println("파일을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
