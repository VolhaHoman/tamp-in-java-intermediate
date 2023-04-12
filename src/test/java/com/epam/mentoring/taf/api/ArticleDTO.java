package com.epam.mentoring.taf.api;

import com.epam.mentoring.taf.service.YamlReader;

public class ArticleDTO {

    public static final YamlReader READER = new YamlReader();
    private final String title;
    private final String description;
    private final String body;
    private final String[] tagList;

    public static final String JSON_BODY = "{\"user\":{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}}";

    public ArticleDTO(ArticleDTOBuilder articleDTOBuilder) {
        this.title = articleDTOBuilder.title;
        this.description = articleDTOBuilder.description;
        this.body = articleDTOBuilder.body;
        this.tagList = articleDTOBuilder.tagList;
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

    public static class ArticleDTOBuilder {

        private String title;
        private String description;
        private String body;
        private String[] tagList;

        public ArticleDTOBuilder(String title, String description, String body) {
            this.title = title;
            this.description = description;
            this.body = body;
        }

        public ArticleDTOBuilder setTagList(String[] tagList) {
            this.tagList = tagList;
            return this;
        }

        public ArticleDTO build() {
            return new ArticleDTO(this);
        }
    }
}
