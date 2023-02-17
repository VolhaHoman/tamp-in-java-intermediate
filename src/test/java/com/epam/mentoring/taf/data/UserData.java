package com.epam.mentoring.taf.data;

import com.epam.mentoring.taf.model.UserDataModel;
import com.epam.mentoring.taf.service.YamlReader;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;

public class UserData {

    public static final YamlReader YAML_READER = new YamlReader();

    private static String generateUniqueId() {
        return RandomStringUtils.randomNumeric(1000);
    }

    public static UserDataDTO generateUserData() throws IOException {
        UserDataModel userDataModel = YAML_READER.readUserData("testUser");
        String uniqueId = generateUniqueId();
        String userEmail = userDataModel.getUserEmail().replace("@", "." + uniqueId + "@");
        String username = userDataModel.getUserName() + uniqueId;
        return new UserDataDTO(username, userEmail, userDataModel.getUserPassword());
    }

    public static UserDataDTO getUserDataFromYaml(String path) throws IOException {
        UserDataModel userDataModel = YAML_READER.readUserData(path);
        return new UserDataDTO(
                userDataModel.getUserName(),
                userDataModel.getUserEmail(),
                userDataModel.getUserPassword()
        );
    }

}
