package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.AbstractTest;
import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

import static com.epam.mentoring.taf.FollowUserTest.ADMIN_EMAIL;
import static com.epam.mentoring.taf.FollowUserTest.ADMIN_PASSWORD;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("UI: Comments Tests")
public class CommentUITest extends AbstractTest {

    public static final String ALL_COMMENT = "ALL_COMMENT";
    public static final String ERROR_MESSAGE = "body can't be blank";
    public static final String COMMENT = "Test";

    private UserDataDTO adminUser;

    @BeforeMethod(description = "admin User", groups = {"regression"})
    public void getAdminUser() {
        try {
            adminUser = UserData.getUserDataFromYaml("adminUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
    }

    @Test(description = "UI: add comment to article", priority = 1, groups = {"regression"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI verification of adding comments")
    @Story("Create new tests for comments functionality")
    public void uiSubmittedCommentVerification() {
        loginPage.clickSignInLink()
                .fillInEmail(adminUser.getUserEmail())
                .fillInPassword(adminUser.getUserPassword())
                .clickSignInBtn();
        homePage.navToUser();
        userProfilePage.selectArt();
        articlePage.enterComment(COMMENT)
                .sendComment();

        Assert.assertEquals(articlePage.getComment(), COMMENT);
        homePage.navToSetting();
        settingPage.logout();
    }

    @Test(description = "UI: add empty comment", priority = 2, groups = {"regression"})
    @Severity(SeverityLevel.MINOR)
    @Story("Create new tests for comments")
    @Description("UI add empty comment")
    public void uiEmptyCommentVerification() {
        loginPage.clickSignInLink()
                .fillInEmail(adminUser.getUserEmail())
                .fillInPassword(adminUser.getUserPassword())
                .clickSignInBtn();
        homePage.navToUser();
        userProfilePage.selectArt();
        articlePage.enterComment("")
                .sendComment();

        Assert.assertEquals(articlePage.getError(), ERROR_MESSAGE);
        homePage.navToSetting();
        settingPage.logout();
    }

    @Test(description = "UI: delete comment from article", priority = 3, groups = {"regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI verification of deleting comments")
    @Story("Create new tests for comments functionality")
    public void uiDeleteCommentVerification() {
        loginPage.clickSignInLink()
                .fillInEmail(adminUser.getUserEmail())
                .fillInPassword(adminUser.getUserPassword())
                .clickSignInBtn();
        homePage.navToUser();
        userProfilePage.selectArt();
        articlePage.deleteComment();

        Assert.assertFalse(articlePage.commentIsNotDisplayed());
        homePage.navToSetting();
        settingPage.logout();
    }

}

