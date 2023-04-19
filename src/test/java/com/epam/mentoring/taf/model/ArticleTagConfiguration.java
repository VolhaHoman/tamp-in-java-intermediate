package com.epam.mentoring.taf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleTagConfiguration {
    private String[] tagList;

    public String[] getTags() {
        return tagList;
    }

    public void setTags(String[] tags) {
        this.tagList = tagList;
    }

//    @Override
//    public String toString() {
//        return "Tag{" +
//                "tags=" + Arrays.toString(tags) +
//                '}';
//    }

}
