package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.dataobject.ApiUserDTO;
import com.epam.mentoring.taf.api.RestAPIClient;
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

import static com.epam.mentoring.taf.ui.page.LoginPage.CREDENTIALS_ERROR_TEXT;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Sign In Tests")
public class UserSignInTest extends UiBaseTest {

    private UserDataDTO defaultUserData;
    private Logger log = LogManager.getLogger();

    @BeforeMethod(description = "Generate default Sign in User")
    public void generateUserData() {
        try {
            defaultUserData = UserData.getUserDataFromYaml("signInUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
    }

    @Test(description = "UI Sign In with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI Sign In with valid credentials")
    @Story("Create layers for UI tests")
    public void uiSignInWithValidCredentialsVerification() {

        logIn(defaultUserData.getUserEmail(), defaultUserData.getUserPassword());
        Assert.assertEquals(homePage.getUsernameAccountNav(), defaultUserData.getUserName());
    }

    @Test(description = "UI Sign In with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI Sign In with invalid credentials")
    @Story("Investigate the issues and fix UserSignInTest")
    public void uiSignInWithInvalidCredentialsVerification() {

        logIn(defaultUserData.getUserEmail(), defaultUserData.getUserPassword() + "1");
        Assert.assertEquals(loginPage.getInvalidCredentialsMessage(), CREDENTIALS_ERROR_TEXT);
    }

    @Test(description = "API Sign In with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Sign In with valid credentials")
    @Story("Investigate the issues and fix UserSignInTest")
    public void apiVerificationHappyPath() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO, redirection.getRedirectionUrl(API_LOGIN), log);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
    }

    @Test(description = "API Sign In with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Sign In with invalid credentials")
    @Story("Investigate the issues and fix UserSignInTest")
    public void apiVerificationUnhappyPath() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword() + "1")
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO, redirection.getRedirectionUrl(API_LOGIN), log);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_FORBIDDEN);
    }

}
