package com.epam.mentoring.taf.data;

import com.epam.mentoring.taf.api.ApiUserDTO;
import com.epam.mentoring.taf.model.User;
import com.epam.mentoring.taf.service.YamlReader;

import java.io.IOException;

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
    public static final String WRONG_PASSWORD = "wrong_password";



    static YamlReader reader = new YamlReader();

    public static ApiUserDTO getSignInUser() throws IOException {
        ApiUserDTO defaultUserModel = reader.readUserData();
        return new ApiUserDTO(
                defaultUserModel.getEmail(),
                defaultUserModel.getPassword()
        );
    }


}
