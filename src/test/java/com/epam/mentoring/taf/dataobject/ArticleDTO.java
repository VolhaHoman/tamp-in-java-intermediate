package com.epam.mentoring.taf.dataobject;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleDTO {

    private final String title;
    private final String description;
    private final String body;
    private final List<String> tagList;

    private static final String JSON_BODY = "{\"article\":{\"title\":\"%s\", \"description\":\"%s\", \"body\":\"%s\", \"tagList\":[%s]}}";

    public ArticleDTO(String title, String description, String body, List<String> tagList) {
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

    public List<String> getTagList() {
        return tagList;
    }

    public String articleToString() {
        return String.format(JSON_BODY, title, description, body, tagList.stream()
                .collect(Collectors.joining("\", \"", "\"", "\"")));
    }
}
