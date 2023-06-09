package com.epam.mentoring.taf.tests.api;

import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.dataobject.ApiUserDTO;
import com.epam.mentoring.taf.dataobject.ResponseDTO;
import com.epam.mentoring.taf.dataobject.UserData;
import com.epam.mentoring.taf.dataobject.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.epam.mentoring.taf.tests.AuthorizationUserBase.API_USERS;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("API: Sign Up Tests")
public class UserSignUpAPITest {

    Logger logger = LogManager.getLogger();

    public static final String BLANK_ERROR_TEXT = "can't be blank";
    public static final String ALREADY_TAKEN_ERROR_TEXT = "has already been taken";
    private UserDataDTO userDataDTO;
    private UserDataDTO defaultUserData;

    @BeforeMethod(description = "Generate Test User")
    public void generateUserData() {
        try {
            userDataDTO = UserData.generateUserData();
            defaultUserData = UserData.getUserDataFromYaml("testUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
    }

    @Test(description = "API Sign Up with new credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("API Sign Up with new credentials")
    @Story("Create layers for API tests")
    public void apiRegisterVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(userDataDTO.getUserEmail(), userDataDTO.getUserPassword())
                .setUsername(userDataDTO.getUserName())
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO, API_USERS, logger);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
    }

    @Test(description = "API Sign Up with existing credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Sign Up with existing credentials")
    @Story("Create layers for API tests")
    public void apiAlreadyRegisteredUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .setUsername(defaultUserData.getUserName())
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO, API_USERS, logger);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response, logger);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), ALREADY_TAKEN_ERROR_TEXT);
    }

    @Test(description = "API Sign Up with empty username")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Sign Up with empty username")
    @Story("Create layers for API tests")
    public void apiBlankUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .setUsername("")
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO, API_USERS, logger);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response, logger);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), BLANK_ERROR_TEXT);
    }

}
