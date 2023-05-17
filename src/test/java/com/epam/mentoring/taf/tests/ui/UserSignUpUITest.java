package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.dataobject.UserData;
import com.epam.mentoring.taf.dataobject.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.tests.uihelper.OpenClose;
import com.epam.mentoring.taf.tests.uihelper.PageLoader;
import com.epam.mentoring.taf.ui.page.HomePage;
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
public class UserSignUpUITest
        implements OpenClose, PageLoader {

    Logger logger = LogManager.getLogger();

    public static final String BLANK_ERROR_TEXT = "username can't be blank";
    private UserDataDTO userDataDTO;

    @BeforeMethod(description = "Generate Test User")
    public void generateUserData() {
        try {
            userDataDTO = UserData.generateUserData();
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
    }

    @Test(description = "UI Sign Up with new credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI Sign Up with new credentials")
    @Story("Investigate the issues and fix UserSignUpTest")
    public void signUpVerification() {
        HomePage homePage = homePage(logger);
        homePage.clickSignUpLink();
        registerPage(logger).fillInUsername(userDataDTO.getUserName())
                .fillInEmail(userDataDTO.getUserEmail())
                .fillInPassword(userDataDTO.getUserPassword())
                .clickSignUpBtn();

        Assert.assertEquals(homePage.getUsernameAccountNav(), userDataDTO.getUserName());
    }

    @Test(description = "UI Sign Up with empty Username")
    @Severity(SeverityLevel.NORMAL)
    @Description("UI Sign Up with empty Username")
    @Story("Organise “User Sign Up” and “Comments” tests into test suites")
    public void signUpBlankUserVerification() {
        homePage(logger).clickSignUpLink();
        registerPage(logger).fillInUsername("")
                .fillInEmail(userDataDTO.getUserEmail())
                .fillInPassword(userDataDTO.getUserPassword())
                .clickSignUpBtn();

        Assert.assertEquals(loginPage(logger).getInvalidCredentialsMessage(), BLANK_ERROR_TEXT);
    }

}
