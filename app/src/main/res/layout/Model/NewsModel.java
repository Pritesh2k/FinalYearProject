package com.example.finalyearproject.Model;

public class NewsModel {

    String title;
    String author;
    String content;
    String description;
    String url;
    String publisDate;
    String urlToImage;
    String source;

    public NewsModel(String title, String author, String content, String description, String url, String publisDate, String urlToImage, String source) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.description = description;
        this.url = url;
        this.publisDate = publisDate;
        this.urlToImage = urlToImage;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getPublisDate() {
        return publisDate;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getSource() {
        return source;
    }
}
