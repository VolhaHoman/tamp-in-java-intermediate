package com.epam.mentoring.taf.tests.api;

import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.dataobject.ApiUserDTO;
import com.epam.mentoring.taf.dataobject.UserData;
import com.epam.mentoring.taf.dataobject.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.util.Redirection;
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

import static com.epam.mentoring.taf.tests.AuthorizationUserBase.API_LOGIN;
import static com.epam.mentoring.taf.tests.AuthorizationUserBase.RESPONSE;
import static com.epam.mentoring.taf.util.StorageHelper.rememberTheResponse;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsTheResponse;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Sign In Tests")
public class UserSignInTestApi {

    Logger logger = LogManager.getLogger();

    private UserDataDTO defaultUserData;

    @BeforeMethod(description = "Generate default Sign in User")
    public void generateUserData() {
        try {
            defaultUserData = UserData.getUserDataFromYaml("signInUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
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
        Response response = restAPIClient.sendApiRequest(apiUserDTO, Redirection.getRedirectionUrl(API_LOGIN),
                logger);
        rememberTheResponse(RESPONSE, response);

        verifyStatusCodeIsOk();
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
        Response response = restAPIClient.sendApiRequest(apiUserDTO, Redirection.getRedirectionUrl(API_LOGIN),
                logger);
        rememberTheResponse(RESPONSE, response);

        verifyStatusCodeIsForbidden();
    }

    @Step
    private void verifyStatusCodeIsOk() {
        Response response = whatIsTheResponse(RESPONSE);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "Status code is ok");
    }

    @Step
    private void verifyStatusCodeIsForbidden() {
        Response response = whatIsTheResponse(RESPONSE);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_FORBIDDEN, "Status code is 403");
    }

}
