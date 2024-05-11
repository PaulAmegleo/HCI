package com.example.hcitest;

import java.util.List;

public class Poem {
    private String title;
    private String author;
    private List<String> lines;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public Poem(String title, String author, List<String> lines) {
        this.title = title;
        this.author = author;
        this.lines = lines;
    }
}