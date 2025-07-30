package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        WiseManager wm = new WiseManager();
        System.out.println("== 명언 앱 ==");
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.print("명령) ");
            String cmd = sc.nextLine().trim();

            if(cmd.equals("종료")) {
                wm.exit();
                break;
            } else if(cmd.equals("등록")) {
                wm.addWise(sc);
            }else if(cmd.equals("목록")){
                wm.showWiseList();
            }else if(cmd.contains("삭제") || cmd.contains("수정")){
                Map<String, String> queryMap = makeQueryMap(cmd);

                try {
                    int idToProcess = Integer.parseInt(queryMap.get("id"));

                    if (cmd.contains("삭제")) wm.removeWise(idToProcess);
                    else if(cmd.contains("수정")) wm.updateWise(sc, idToProcess);
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                }
            }
        }

        File dir = new File("db.wiseSaying");
        if (!dir.exists()) dir.mkdirs();

        for(Wise wise : wm.getWiseList()) {
            String jsonString = "{\"id\":"+wise.getId()+", \"content\":\""+wise.getContent()+"\", \"author\":\""+wise.getAuthor()+"\"}";
            File file = new File(dir, "%d.json".formatted(wise.getId()));
            try(FileWriter writer = new FileWriter(file)){
                writer.write(jsonString);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        sc.close();
    }

    static Map<String, String> makeQueryMap(String cmd) {
        int queryStartIndex = cmd.indexOf("?");
        if(queryStartIndex == -1) {
            return null;
        }

        String query = cmd.substring(queryStartIndex + 1).trim();
        Map<String, String> queryMap = new HashMap<>();
        String[] queryArray = query.split("&");
        for (String q : queryArray) {
            String[] keyValue = q.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";
            queryMap.put(key, value);
        }
        return queryMap;
    }
}

class WiseManager {
    static int id = 1;
    List<Wise> wiseList = new ArrayList<>();

    private Wise getWiseById(int id) {
        return wiseList.stream()
                .filter(wise -> wise.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Wise> getWiseList() {
        List<Wise> sortedWiseList = wiseList.stream()
                .sorted((w1, w2) -> Integer.compare(w2.getId(), w1.getId()))
                .toList();
        return sortedWiseList;
    }

    void exit() {
        System.out.println("== 명언 앱 종료 ==");
        System.out.println(wiseList.stream().map(Wise::getId).toList());
    }

    void addWise(Scanner sc) {
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();
        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        Wise wise = new Wise(content, author, this.id);
        this.wiseList.add(wise);
        System.out.println(id + "번 명언이 등록되었습니다.");
        this.id++;
    }

    void removeWise(int id) {
        Wise wiseToDelete = wiseList.stream()
                .filter(wise -> wise.getId() == id)
                .findFirst()
                .orElse(null);

        if (wiseToDelete == null) {
            System.out.println(id+ "번 명언은 존재하지 않습니다.");
        } else {
            wiseList.remove(wiseToDelete);
            System.out.println(id + "번 명언이 삭제되었습니다.");
        }
    }

    void updateWise(Scanner sc, int id) {
        Wise wiseToUpdate = getWiseById(id);

        if (wiseToUpdate == null){
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        } else {
            System.out.println("명언(기존) : " + wiseToUpdate.getContent());
            System.out.print("명언 : ");
            String newContent = sc.nextLine().trim();
            System.out.println("작가(기존) : " + wiseToUpdate.getAuthor());
            System.out.print("작가 : ");
            String newAuthor = sc.nextLine().trim();

            wiseToUpdate.setContent(newContent);
            wiseToUpdate.setAuthor(newAuthor);
            System.out.println(id + "번 명언이 수정되었습니다.");
        }
    }

    void showWiseList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("---------------------");

        for(Wise wise : this.getWiseList()){
            System.out.println("%d / %s / %s".formatted(wise.getId(), wise.getAuthor(), wise.getContent()));
        }
    }
}

class Wise {
    private String content;
    private String author;
    private int id;

    Wise(String wise, String author, int id) {
        this.content = wise;
        this.author = author;
        this.id = id;
    }

    String getContent(){
        return this.content;
    }

    void setContent(String content){
        this.content = content;
    }

    String getAuthor(){
        return this.author;
    }

    void setAuthor(String author){
        this.author = author;
    }

    int getId(){
        return this.id;
    }
}
