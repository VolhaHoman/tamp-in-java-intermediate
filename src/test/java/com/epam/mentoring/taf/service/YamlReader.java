package com.epam.mentoring.taf.service;

import com.epam.mentoring.taf.model.Configuration;
import com.epam.mentoring.taf.model.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlReader {

    public String[] readTags() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file = new File("src/test/resources/testTags.yml");
        Tag tag = mapper.readValue(file, Tag.class);
        return tag.getTags();
    }

    public String[] readBrowser() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file = new File("src/test/resources/configuration.yml");
        Configuration browser = mapper.readValue(file, Configuration.class);
        return browser.getBrowser();
    }

}
