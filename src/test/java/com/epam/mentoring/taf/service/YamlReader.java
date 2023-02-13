package com.epam.mentoring.taf.service;

import com.epam.mentoring.taf.model.BrowserConfiguration;
import com.epam.mentoring.taf.model.TagConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class YamlReader {

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public String[] readTags() throws IOException {
        File file = new File("src/test/resources/listOfTags.yml");
        TagConfiguration tag = mapper.readValue(file, TagConfiguration.class);
        return tag.getTags();
    }

    public String readBrowser() throws IOException, ConfigurationException {
        try (InputStream inputStream = YamlReader.class.getClassLoader().getResourceAsStream("browserConfiguration.yml")) {
            BrowserConfiguration browser = mapper.readValue(inputStream, BrowserConfiguration.class);
            String driverForUse = browser.getBrowser();
            return driverForUse != null ? driverForUse : "";
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load browser configuration");
        }
    }

}
