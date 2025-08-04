package com.back.domain.wiseSaying.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WiseSaying {
    private String content;
    private String author;
    private int id = 0;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public WiseSaying(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreateDateStr() {
        return createDate.format(formatter);
    }
    public String getModifyDateStr() {
        return modifyDate.format(formatter);
    }

    public boolean isNew() {
        return id == 0;
    }
}