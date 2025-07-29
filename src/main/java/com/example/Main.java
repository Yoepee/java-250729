package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("명령) ");
            String cmd = sc.nextLine().trim();
            System.out.println("입력한 명령: " + cmd);
            if(cmd.equals("종료")) break;
            if(cmd.equals("등록")) {
                System.out.print("명언 : ");
                String wise = sc.nextLine().trim();
                System.out.print("작가 : ");
                String author = sc.nextLine().trim();
                System.out.println("번 명언을 등록합니다."+
                                   " 명언: " + wise + ", 작가: " + author);
            }
        }

        sc.close();
    }
}

class Wise {
    private String wise;
    private String author;
    private int id;


}
