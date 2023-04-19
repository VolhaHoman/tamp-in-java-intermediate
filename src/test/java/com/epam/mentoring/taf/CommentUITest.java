package com.epam.mentoring.taf;

import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("UI: Comments Tests")
public class CommentUITest extends AbstractTest {

    public static final String ALL_COMMENT = "ALL_COMMENT";
    public static final String ERROR_MESSAGE = "body can't be blank";
    public static final String COMMENT = "Test";

    private UserDataDTO adminUser;

    @BeforeMethod(description = "Authorization as Admin User")
    public void authorizationAsAdminUser() {
        try {
            adminUser = UserData.getUserDataFromYaml("adminUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
    }

    public void login() {
        loginPage.clickSignInLink()
                .fillInEmail(adminUser.getUserEmail())
                .fillInPassword(adminUser.getUserPassword())
                .clickSignInBtn();
        homePage.navToUser();
        userProfilePage.selectArt();
    }

    @Test(description = "UI: add comment to article", priority = 1, groups = {"regression"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI verification of adding comments")
    @Story("Create new tests for comments functionality")
    public void uiSubmittedCommentVerification() {
        login();
        articlePage.enterComment(COMMENT)
                .sendComment();

        Assert.assertEquals(articlePage.getComment(), COMMENT);
        logOut();
    }

    @Test(description = "UI: add empty comment", priority = 2, groups = {"regression"})
    @Severity(SeverityLevel.MINOR)
    @Story("Create new tests for comments")
    @Description("UI add empty comment")
    public void uiEmptyCommentVerification() {
        login();
        articlePage.enterComment("")
                .sendComment();

        Assert.assertEquals(articlePage.getError(), ERROR_MESSAGE);
        logOut();
    }

    @Test(description = "UI: delete comment from article", priority = 3, groups = {"regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI verification of deleting comments")
    @Story("Create new tests for comments functionality")
    public void uiDeleteCommentVerification() {
        login();
        articlePage.deleteComment();

        Assert.assertTrue(articlePage.commentIsNotDisplayed());
        logOut();
    }

    private static void logOut() {
        homePage.navToSetting();
        settingPage.logout();
    }

}

