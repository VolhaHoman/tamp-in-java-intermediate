package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.model.UserDataModel;
import com.epam.mentoring.taf.service.CsvReader;
import com.epam.mentoring.taf.tests.IAuthorizationTest;
import com.epam.mentoring.taf.tests.ILoggerTest;
import com.epam.mentoring.taf.tests.IRestClient;
import com.epam.mentoring.taf.tests.IYmlReader;
import com.epam.mentoring.taf.tests.uihelper.LoginBaseUI;
import com.epam.mentoring.taf.tests.uihelper.PageLoader;
import com.epam.mentoring.taf.ui.page.CelebPage;
import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.util.DataProviderHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.epam.mentoring.taf.tests.AuthorizationUserBase.*;
import static com.epam.mentoring.taf.util.StorageHelper.*;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Follow User Functionality")
public class FollowUserTest
        implements IYmlReader, IRestClient, PageLoader, LoginBaseUI, ILoggerTest, IAuthorizationTest {

    @BeforeClass
    public void open() {
        DRIVER.get().getWebDriver().get(BASE_URL);
    }

    @AfterClass
    public void close() {
        DRIVER.get().getWebDriver().quit();
    }

    public static final String ADMIN_USERNAME = "ADMIN_USERNAME";
    public static final String RESPONSE = "Response";

    private UserDataModel workUser;

    @BeforeMethod
    public void initUser() throws IOException {
        workUser = READER.get().readUserData("adminUserFollow");
    }

    @DataProvider(name = "csvDataProvider")
    public Object[][] csvDataProvider() throws IOException {
        List<UserDataModel> userDataModels = CsvReader.getUserDataModels();
        return DataProviderHelper.mapToProviderArray(userDataModels);
    }

    @Test(description = "Pre-condition: follow the user", dataProvider = "csvDataProvider", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API follow the user request")
    @Story("Create new tests for users following functionality")
    public void apiFollowUser(UserDataModel model) {

        String path = API_PROFILES + model.getCelebUsername() + FOLLOW_PATH;

        Response response = CLIENT.get().sendPostRequestWithHeaders(path, "", Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        rememberTheResponse(RESPONSE, response);

        verifyStatusCodeIsOk();
        apiVerificationUserIsFollowed(model);
    }

    @Test(description = "UI Sign In as Admin user and verify feed user exists", dataProvider = "csvDataProvider", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI Sign In with Admin credentials")
    @Story("Create new tests for users following functionality")
    public void uiSignInAsAdminUser(UserDataModel model) {
        HomePage homePage = homePage();
        logIn(workUser.getUserEmail(), workUser.getUserPassword(), homePage, loginPage());
        uiVerificationAdminIsSignedIn(model, homePage, workUser.getUserName());
    }

    @Test(description = "Verify feed user is followed", dataProvider = "csvDataProvider", priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI verification of follow functionality")
    @Story("Create new tests for users following functionality")
    public void uiVerifyFollowIsTrue(UserDataModel model) throws InterruptedException {
        homePage().clickCelebUserLink();
        CelebPage celebPage = celebPage();
        celebPage.waitPageIsLoaded();
        uiVerificationUserIsFollowed(model, celebPage);
    }

    @Test(description = "Post-condition: unfollow the user", dataProvider = "csvDataProvider", priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("API unfollow the user request")
    @Story("Create new tests for users following functionality")
    public void apiUnfollowUser(UserDataModel model) {

        String path = API_PROFILES + model.getCelebUsername() + FOLLOW_PATH;

        Response response = CLIENT.get().sendDeleteRequestWithHeaders(path, "", Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        rememberTheResponse(RESPONSE, response);

        verifyStatusCodeIsOk();
    }

    @Test(description = "Verify feed user is unfollowed", dataProvider = "csvDataProvider", priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("API verification the user is not followed")
    @Story("Create new tests for users following functionality")
    public void apiVerifyFollowIsFalse(UserDataModel model) {

        String path = API_PROFILES + model.getCelebUsername();

        Response response = CLIENT.get().sendGetRequestWithHeaders(path, Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        rememberTheResponse(RESPONSE, response);

        apiVerificationUserIsUnfollowed();

    }

    @Step
    private void verifyStatusCodeIsOk() {
        Response response = whatIsTheResponse(RESPONSE);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "Status code is ok");
    }

    @Step
    private void apiVerificationUserIsFollowed(UserDataModel model) {
        Response response = whatIsTheResponse(RESPONSE);
        String following =
                response.getBody().jsonPath().get("profile.following").toString();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(following, "true");
        soft.assertAll("User is followed");

        LOGGER.get().info("Profile name is: " + model.getCelebUsername());
    }

    @Step
    private void uiVerificationAdminIsSignedIn(UserDataModel model, HomePage homePage, String userName) {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(homePage.getUsernameAccountNav(), userName);
        soft.assertEquals(homePage.getYourFeedNav(), "Your Feed");
        soft.assertEquals(homePage.getYourFeedAuthor(), model.getCelebUsername());
        soft.assertAll("Verify that Admin is signed in on UI");

        LOGGER.get().info("Admin user name is: " + userName);
        LOGGER.get().info("Your Feed tab exists: " + homePage.getYourFeedNav());
        LOGGER.get().info("Author name is: " + model.getCelebUsername());
    }

    @Step
    private void uiVerificationUserIsFollowed(UserDataModel model, CelebPage celebPage) {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(celebPage.getAuthorName(), model.getCelebUsername());
        soft.assertEquals(celebPage.getAuthorPostsNav(), "My Posts");
        soft.assertEquals(celebPage.checkUnfollowBtn(), "  Unfollow celeb_Hamster_Boss");
        soft.assertAll("Verify that User is followed on UI");
    }

    @Step
    private void apiVerificationUserIsUnfollowed() {
        Response response = whatIsTheResponse(RESPONSE);
        String following =
                response.getBody().jsonPath().get("profile.following").toString();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        soft.assertEquals(following, "false");
        soft.assertAll("User is unfollowed");
    }

}
