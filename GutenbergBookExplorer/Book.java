package com.student.gutenbergbookexplorer;

public class Book {
    private String title;
    private String author;
    private String url;

    public Book(String title, String author, String url) {
        this.title = title;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return title + " — " + author + " (" + url + ")";
    }
}