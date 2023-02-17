package com.epam.mentoring.taf.service;

import com.epam.mentoring.taf.model.BrowserConfiguration;
import com.epam.mentoring.taf.model.TagConfiguration;
import com.epam.mentoring.taf.model.UserDataModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class YamlReader {
    public final Logger logger = LogManager.getRootLogger();
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public String[] readTags() {
        String[] tags = new String[0];
        try {
            File file = new File("src/test/resources/testData.yml");
            TagConfiguration tag = mapper.readValue(file, TagConfiguration.class);
            tags = tag.getTags();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error log message", e);
        }
        return tags;
    }

    public String readBrowser() throws ConfigurationException {
        try (InputStream inputStream = YamlReader.class.getClassLoader().getResourceAsStream("browserConfiguration.yml")) {
            BrowserConfiguration browser = mapper.readValue(inputStream, BrowserConfiguration.class);
            String driverForUse = browser.getBrowser();
            return driverForUse != null ? driverForUse : "";
        } catch (IOException e) {
            logger.error("Error log message", e);
            throw new ConfigurationException("Failed to load browser configuration");
        }
    }

    public UserDataModel readUserData(String path) throws IOException {
        File file = new File("src/test/resources/testData.yml");
        ObjectReader userReader = mapper.readerFor(UserDataModel.class)
                .at("/" + path);
        return userReader.readValue(file);
    }

}
