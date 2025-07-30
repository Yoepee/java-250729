package com.back.domain.system.controller;

import java.util.HashMap;
import java.util.Scanner;

public class SystemController {
    Scanner sc;
    HashMap<String, String> queryParams;

    public SystemController(Scanner sc) {
        this.sc = sc;
    }

    public void start() {
        System.out.println("== 명언 앱 ==");
    }

    public void stop() {
        sc.close();
    }

    public String getCommand() {
        System.out.print("명령) ");
        String cmd = sc.nextLine().trim();

        queryParams = null;
        int indexOfQuestion = cmd.indexOf("?");
        String pureCmd = indexOfQuestion == -1 ? cmd : cmd.substring(0, indexOfQuestion).trim();

        // 삭제, 수정일 경우만 파라미터 파싱 시도
        if (indexOfQuestion != -1) {
            getQueryParamsByCommand(cmd);
        }

        return pureCmd;
    }

    private void getQueryParamsByCommand(String cmd) {
        queryParams = new HashMap<>();
        int indexOfQuestionMark = cmd.indexOf("?");
        if (indexOfQuestionMark == -1) return;

        String query = cmd.substring(indexOfQuestionMark + 1);
        if (query.isEmpty()) return; // No query parameters found

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                queryParams.put(key, value);
            }
        }
    }

    public int getIdOrWarn() {
        if (queryParams == null || !queryParams.containsKey("id")) {
            System.out.println("id 파라미터가 필요합니다. 예) 삭제?id=3");
            return -1;
        }

        try {
            return Integer.parseInt(queryParams.get("id"));
        } catch (NumberFormatException e) {
            System.out.println("id는 숫자여야 합니다.");
            return -1;
        }
    }

    public String getKeywordType() {
        if (queryParams == null || !queryParams.containsKey("keywordType")) {
            return null;
        }

        String keywordType = queryParams.get("keywordType").trim();
        if (keywordType.equals("명언")) return "content";
        else if (keywordType.equals("작가")) return "author";
        else if (!keywordType.equals("content") && !keywordType.equals("author")) {
            System.out.println("keywordType은 'content' 또는 'author' 만 가능합니다.");
            return null;
        }

        return keywordType;
    }

    public String getKeyword() {
        if (queryParams == null || !queryParams.containsKey("keyword")) {
            return null;
        }

        String keyword = queryParams.get("keyword").trim();
        return keyword;
    }

    public int getPage() {
        if (queryParams == null || !queryParams.containsKey("page")) {
            return 1; // 기본 페이지는 1
        }

        try {
            return Integer.parseInt(queryParams.get("page"));
        } catch (NumberFormatException e) {
            return 1; // 오류 발생 시 기본 페이지로 설정
        }
    }
}
