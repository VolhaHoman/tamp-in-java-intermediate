package com.epam.mentoring.taf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TagConfiguration {

    private String[] tags;

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String toString() {
        return "Tag{" +
                "tags=" + Arrays.toString(tags) +
                '}';
    }
}
