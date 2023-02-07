package com.epam.mentoring.taf.model;

import java.util.Arrays;

public class Tag {

    private String[] tags;

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tags=" + Arrays.toString(tags) +
                '}';
    }
}
