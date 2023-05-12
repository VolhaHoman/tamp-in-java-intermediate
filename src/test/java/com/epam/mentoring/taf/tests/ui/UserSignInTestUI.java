package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.dataobject.UserData;
import com.epam.mentoring.taf.dataobject.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.tests.UiBaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.epam.mentoring.taf.ui.page.LoginPage.CREDENTIALS_ERROR_TEXT;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Sign In Tests")
public class UserSignInTestUI extends UiBaseTest {

    private UserDataDTO defaultUserData;

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
        uiVerificationUserWithValidCredsIsSignedIn();
    }

    @Test(description = "UI Sign In with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI Sign In with invalid credentials")
    @Story("Investigate the issues and fix UserSignInTest")
    public void uiSignInWithInvalidCredentialsVerification() {

        logIn(defaultUserData.getUserEmail(), defaultUserData.getUserPassword() + "1");
        uiVerificationUserWithInvalidCredsCannotSignIn();
    }

    @Step
    private void uiVerificationUserWithValidCredsIsSignedIn() {
        Assert.assertEquals(homePage.getUsernameAccountNav(), defaultUserData.getUserName(),
                "User is signed in");
    }

    @Step
    private void uiVerificationUserWithInvalidCredsCannotSignIn() {
        Assert.assertEquals(loginPage.getInvalidCredentialsMessage(), CREDENTIALS_ERROR_TEXT);
    }

}
