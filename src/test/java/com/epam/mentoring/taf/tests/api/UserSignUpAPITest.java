package com.epam.mentoring.taf.tests.api;

import com.epam.mentoring.taf.AbstractTest;
import com.epam.mentoring.taf.api.ApiUserDTO;
import com.epam.mentoring.taf.api.ResponseDTO;
import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("API: Sign Up Tests")
public class UserSignUpAPITest extends AbstractTest {

    private UserDataDTO userDataDTO;

    private UserDataDTO defaultUserData;

    private Logger log = LogManager.getLogger();

    @BeforeClass(description = "Generate Test User", groups = {"smoke", "regression"})
    public void generateUserData() {
        try {
            userDataDTO = UserData.generateUserData();
            defaultUserData = UserData.getUserDataFromYaml("testUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
    }

    @Test(description = "API Sign Up with new credentials", groups = {"regression"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("API Sign Up with new credentials")
    @Story("Create layers for API tests")
    public void apiRegisterVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(userDataDTO.getUserEmail(), userDataDTO.getUserPassword())
                .setUsername(userDataDTO.getUserName())
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO, API_USERS, log);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "API Sign Up with existing credentials", groups = {"regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Sign Up with existing credentials")
    @Story("Create layers for API tests")
    public void apiAlreadyRegisteredUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .setUsername(defaultUserData.getUserName())
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO, API_USERS, log);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response, log);
        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), "has already been taken");
    }

    @Test(description = "API Sign Up with empty username", groups = {"regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Sign Up with empty username")
    @Story("Create layers for API tests")
    public void apiBlankUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .setUsername("")
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO, API_USERS, log);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response, log);
        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), "can't be blank");
    }

}
