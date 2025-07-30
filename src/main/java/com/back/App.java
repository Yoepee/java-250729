package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Scanner;

public class App {
    Scanner sc = new Scanner(System.in);
    SystemController systemController = new SystemController(sc);
    WiseSayingController wiseSayingController = new WiseSayingController(new WiseSayingService(new WiseSayingRepository()));

    public void run() {
        systemController.start();
        wiseSayingController.load();

        while (true) {
            String cmd = systemController.getCommand();
            if (cmd.equals("종료")) break;

            processCommand(cmd, systemController, wiseSayingController, sc);
        }

        systemController.stop();
    }

    private static void processCommand(String cmd, SystemController systemController, WiseSayingController wiseSayingController, Scanner sc) {
        switch (cmd) {
            case "등록" -> wiseSayingController.add(sc);
            case "목록" -> processSearchCommand(systemController, wiseSayingController);
            case "빌드" -> wiseSayingController.build();
            case "삭제", "수정" -> processModifyCommand(cmd, systemController, wiseSayingController, sc);
            default -> System.out.println("알 수 없는 명령입니다.");
        }
    }

    private static void processModifyCommand(String cmd, SystemController systemController, WiseSayingController wiseSayingController, Scanner sc) {
        int id = systemController.getIdOrWarn();
        if (id == -1) return;

        if (cmd.equals("수정")) {
            wiseSayingController.update(id, sc);
        } else {
            wiseSayingController.remove(id);
        }
    }

    private static void processSearchCommand(SystemController systemController, WiseSayingController wiseSayingController) {
        String keywordType = systemController.getKeywordType();
        String keyword = systemController.getKeyword();

        if (keywordType != null && keyword != null) {
            wiseSayingController.showByKeyword(keywordType, keyword);
        }else {
            wiseSayingController.showAll();
        }
    }
}
