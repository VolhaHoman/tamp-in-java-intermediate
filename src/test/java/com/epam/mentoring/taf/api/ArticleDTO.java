package com.epam.mentoring.taf.api;

import com.epam.mentoring.taf.service.YamlReader;

public class ArticleDTO {

    public static final YamlReader READER = new YamlReader();
    private final String title;
    private final String description;
    private final String body;
    private final String[] tagList;

    public static final String JSON_BODY = "{\"article\":{\"title\":\"%s\",\"description\":\"%s\",\"body\":\"%s\",\"tagList\":[\"%s\", \"%s\"]}}";

    public ArticleDTO(String title, String description, String body, String[] tagList) {
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

    public String toString() {
        return String.format(JSON_BODY, title, description, body, tagList);
    }
}
