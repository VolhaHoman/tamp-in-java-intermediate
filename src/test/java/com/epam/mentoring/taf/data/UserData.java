package com.epam.mentoring.taf.data;

import com.epam.mentoring.taf.model.UserDataModel;
import com.epam.mentoring.taf.service.YamlReader;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;

public class UserData {

    public static final String DEFAULT_USERNAME = "Tom Marvolo Riddle";
    public static final String DEFAULT_EMAIL = "tom_marvolo@example.com";
    public static final String DEFAULT_PASSWORD = "Voldemort";

    public static final YamlReader YAML_READER = new YamlReader();

    private static String generateUniqueId() {
        return RandomStringUtils.randomNumeric(1000);
    }

    public static UserDataDTO generateUserData() throws IOException {
        UserDataModel userDataModel = YAML_READER.readUserData();
        String uniqueId = generateUniqueId();
        String userEmail = userDataModel.getUserEmail().replace("@", "." + uniqueId + "@");
        String username = userDataModel.getUserName() + uniqueId;
        return new UserDataDTO(username, userEmail, userDataModel.getUserPassword());
    }

    public static UserDataDTO getDefaultUserData() throws IOException {
        UserDataModel userDataModel = YAML_READER.readUserData();
        return new UserDataDTO(
                userDataModel.getUserName(),
                userDataModel.getUserEmail(),
                userDataModel.getUserPassword()
        );
    }
}
