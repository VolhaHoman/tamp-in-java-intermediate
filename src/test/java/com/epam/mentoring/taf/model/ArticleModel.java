package com.epam.mentoring.taf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"title", "description", "body", "tagList"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleModel {

    private String title;
    private String description;
    private String body;
    private String[] tagList;

    public ArticleModel(String title, String description, String body, String[] tagList) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public String[] getTagList() {
        return tagList;
    }

    public ArticleModel() {
    }
}
