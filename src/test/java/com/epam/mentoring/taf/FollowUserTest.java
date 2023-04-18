package com.epam.mentoring.taf;

import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.model.UserDataModel;
import com.epam.mentoring.taf.service.CsvReader;
import com.epam.mentoring.taf.util.DataProviderHelper;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Follow User Functionality")
public class FollowUserTest extends AbstractTest {

    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String ADMIN_EMAIL = "ADMIN_USER";
    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    public static final String ADMIN_USERNAME = "ADMIN_USERNAME";
    private Logger log = LogManager.getLogger();

    @DataProvider(name = "csvDataProvider")
    public Object[][] csvDataProvider() throws IOException {
        List<UserDataModel> userDataModels = CsvReader.getUserDataModels();
        return DataProviderHelper.mapToProviderArray(userDataModels);
    }

    @Test(description = "Pre-condition: follow the user", dataProvider = "csvDataProvider", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API follow the user request")
    @Story("Create new tests for users following functionality")
    public void followUser(UserDataModel model) {

        String path = API_PROFILES + model.getCelebUsername() + FOLLOW_PATH;

        Response response = client.sendPostRequestWithHeaders(path, "", Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
    }

    @Test(description = "UI Sign In as Admin user and verify feed user exists", dataProvider = "csvDataProvider", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI Sign In with Admin credentials")
    @Story("Create new tests for users following functionality")
    public void uiSignInAsAdminUser(UserDataModel model) {

        loginPage.clickSignInLink()
                .fillInEmail(StorageHelper.whatIsThe(ADMIN_EMAIL))
                .fillInPassword(StorageHelper.whatIsThe(ADMIN_PASSWORD))
                .clickSignInBtn();

        Assert.assertEquals(homePage.getUsernameAccountNav(), StorageHelper.whatIsThe(ADMIN_USERNAME));
        Assert.assertEquals(homePage.getYourFeedNav(), "Your Feed");
        Assert.assertEquals(homePage.getYourFeedAuthor(), model.getCelebUsername());

        log.info("Admin user name is: " + StorageHelper.whatIsThe(ADMIN_USERNAME));
        log.info("Your Feed tab exists: " + homePage.getYourFeedNav());
        log.info("Author name is: " + model.getCelebUsername());
    }

    @Test(description = "Verify feed user is followed", dataProvider = "csvDataProvider", priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI verification of follow functionality")
    @Story("Create new tests for users following functionality")
    public void uiVerifyFollowIsTrue(UserDataModel model) throws InterruptedException {

        homePage.clickCelebUserLink();

        celebPage.waitPageIsLoaded();

        Assert.assertEquals(celebPage.getAuthorName(), model.getCelebUsername());
        Assert.assertEquals(celebPage.getAuthorPostsNav(), "My Posts");
        Assert.assertEquals(celebPage.checkUnfollowBtn(), "  Unfollow celeb_Hamster_Boss");
    }

    @Test(description = "Post-condition: unfollow the user", dataProvider = "csvDataProvider", priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("API unfollow the user request")
    @Story("Create new tests for users following functionality")
    public void unfollowUser(UserDataModel model) {

        String path = API_PROFILES + model.getCelebUsername() + FOLLOW_PATH;

        Response response = client.sendDeleteRequestWithHeaders(path, Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
    }

}
