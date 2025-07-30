package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("명령) ");
            String cmd = sc.nextLine();
            if (cmd.equals("종료")) break;
        }

        sc.close();
    }
}
