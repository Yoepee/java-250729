package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class App {
    static final int PAGE_SIZE = 5;
    Scanner sc = new Scanner(System.in);
    SystemController systemController = new SystemController(sc);
    WiseSayingController wiseSayingController = new WiseSayingController(sc);

    public void run() {
        systemController.start();
        wiseSayingController.load();

        while (true) {
            String cmd = systemController.getCommand();
            if (cmd.equals("종료")) break;

            processCommand(cmd);
        }

        systemController.stop();
    }

    private void processCommand(String cmd) {
        switch (cmd) {
            case "등록" -> wiseSayingController.add();
            case "목록" -> processSearchCommand();
            case "빌드" -> wiseSayingController.build();
            case "삭제", "수정" -> processModifyCommand(cmd);
            default -> System.out.println("알 수 없는 명령입니다.");
        }
    }

    private void processModifyCommand(String cmd) {
        int id = systemController.getRq().getParamAsInt("id", -1);
        if (id == -1) {
            System.out.println("유효하지 않은 id 입니다.");
            return;
        }

        Map<String, Runnable> actionMap = Map.of(
                "수정", () -> wiseSayingController.update(id),
                "삭제", () -> wiseSayingController.remove(id)
        );

        actionMap.get(cmd).run();
    }

    private void processSearchCommand() {
        int page = systemController.getRq().getParamAsInt("page", 1);
        String keywordType = systemController.getRq().getParam("keywordType", null);
        String keyword = systemController.getRq().getParam("keyword", null);

        Set<String> validKeywordTypes = Set.of("content", "author");

        // keywordType만 있고 keyword가 null이거나, keywordType이 유효하지 않은 경우 처리
        if (keywordType != null && (!validKeywordTypes.contains(keywordType) || keyword == null)) {
            System.out.println("keywordType은 'content' 또는 'author' 만 가능하며, keyword도 함께 입력되어야 합니다.");
            return;
        }

        wiseSayingController.showList(PAGE_SIZE, page, keywordType, keyword);
    }
}
