package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.ApiUserDTO;
import com.epam.mentoring.taf.api.ResponseDTO;
import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.listeners.TestListener;
import io.qameta.allure.*;
import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.ui.page.LoginPage;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Listeners({ TestListener.class })
@Feature("Sign Up Tests")
import java.io.IOException;

public class UserSignUpTest extends AbstractTest {

    private UserDataDTO userDataDTO;
    private UserDataDTO defaultUserData;

    @BeforeMethod
    public void generateUserData() {
        try {
            userDataDTO = UserData.generateUserData();
            defaultUserData = UserData.getDefaultUserData();
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }

    }

    @Test(description = "UI Sign Up with new credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI Sign Up with new credentials")
    @Story("Investigate the issues and fix UserSignUpTest")
    public void signUpVerification() {
        LoginPage loginPage = new LoginPage(baseUrl);
        loginPage.clickSignUpLink()
                .fillInUsername(userDataDTO.getUserName())
                .fillInEmail(userDataDTO.getUserEmail())
                .fillInPassword(userDataDTO.getUserPassword())
                .clickSignUpBtn();

        HomePage homePage = new HomePage();
        Assert.assertEquals(homePage.getUsernameAccountNav(), userDataDTO.getUserName());
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
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void apiAlreadyRegisteredUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .setUsername(defaultUserData.getUserName())
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response);
        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), "has already been taken");
    }

    @Test(description = "API Sign Up with existing credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Sign Up with existing credentials")
    @Story("Create layers for API tests")
    public void apiBlankUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .setUsername("")
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response);
        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), "can't be blank");
    }
}
