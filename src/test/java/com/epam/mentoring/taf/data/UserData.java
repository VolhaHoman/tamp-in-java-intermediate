package com.epam.mentoring.taf.data;

import org.apache.commons.lang3.RandomStringUtils;

public class UserData {

    public static final String DEFAULT_USERNAME = "Tom Marvolo Riddle";
    public static final String DEFAULT_EMAIL = "tom_marvolo@example.com";
    public static final String DEFAULT_PASSWORD = "Voldemort";

    private static final String NAME = "Test User";
    private static final String EMAIL = "test_user@example.com";
    private static final String PASSWORD = "test_password";


    private static String generateUniqueId() {
     return RandomStringUtils.randomNumeric(1000);
    }

    public static UserDataDTO generateUserData() {
        String uniqueId = generateUniqueId();
        String userEmail = EMAIL.replace("@", "." + uniqueId + "@");
        String username = NAME + uniqueId;
        return new UserDataDTO(username, userEmail, PASSWORD);
    }

    public static UserDataDTO getDefaultUserData() {
        return new UserDataDTO(NAME, EMAIL, PASSWORD);
    }
}
