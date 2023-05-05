package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.UiBaseTest;
import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("UI: Sign Up Tests")
public class UserSignUpUITest extends UiBaseTest {

    private UserDataDTO userDataDTO;
    private UserDataDTO defaultUserData;
    private Logger log = LogManager.getLogger();

    @BeforeMethod(description = "Generate Test User")
    public void generateUserData() {
        try {
            userDataDTO = UserData.generateUserData();
            defaultUserData = UserData.getUserDataFromYaml("testUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
    }

    @Test(description = "UI Sign Up with new credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI Sign Up with new credentials")
    @Story("Investigate the issues and fix UserSignUpTest")
    public void signUpVerification() {
        homePage.clickSignUpLink();
        registerPage.fillInUsername(userDataDTO.getUserName())
                .fillInEmail(userDataDTO.getUserEmail())
                .fillInPassword(userDataDTO.getUserPassword())
                .clickSignUpBtn();

        Assert.assertEquals(homePage.getUsernameAccountNav(), userDataDTO.getUserName());
    }

}
