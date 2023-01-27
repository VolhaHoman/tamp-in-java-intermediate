package com.epam.mentoring.taf;

import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.ui.page.LoginPage;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.epam.mentoring.taf.data.UserData.*;
import static com.epam.mentoring.taf.ui.page.LoginPage.CREDENTIALS_ERROR_TEXT;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class UserSignInTest extends AbstractTest {

    private static final String LOGIN_URL = "/api/users/login";
    public static final String FULL_URL = API_URL + LOGIN_URL;
    public static final String LOCATION_HEADER_NAME = "Location";
    public static final String WRONG_PASSWORD = "wrong_password";
    public static final String REQUEST_MASK_JSON_BODY = "{\"user\":{\"email\":\"%s\",\"password\":\"%s\"}}";
    public static final String EMAIL_OR_PASSWORD_JSON_PATH = "errors.'email or password'";
    public static final String USER_EMAIL_JSON_PATH = "user.email";
    public static final String INVALID_RESPONSE = "is invalid";

    @Test
    public void uiSignInWithValidCredentialsVerification() {
        LoginPage loginPage = new LoginPage(baseUrl);
        loginPage.clickSignInLink()
                .fillInEmail()
                .fillInPassword(DEFAULT_PASSWORD)
                .clickSignInBtn();
        HomePage homePage = new HomePage();
        Assert.assertEquals(homePage.getUsernameAccountNav(), DEFAULT_USERNAME);
    }

    @Test
    public void uiSignInWithInvalidCredentialsVerification() {
        LoginPage loginPage = new LoginPage(baseUrl);
        loginPage.clickSignInLink()
                .fillInEmail()
                .fillInPassword(WRONG_PASSWORD)
                .clickSignInBtn();
        Assert.assertEquals(loginPage.getInvalidCredentialsMessage(), CREDENTIALS_ERROR_TEXT);
    }

    @Test
    public void apiVerificationHappyPath() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(String.format(REQUEST_MASK_JSON_BODY, DEFAULT_EMAIL, DEFAULT_PASSWORD))
                .post(getRedirectionUrl())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(USER_EMAIL_JSON_PATH, is(DEFAULT_EMAIL));
    }

    @Test
    public void apiVerificationUnhappyPath() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(String.format(REQUEST_MASK_JSON_BODY, DEFAULT_EMAIL, WRONG_PASSWORD))
                .post(getRedirectionUrl())
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body(EMAIL_OR_PASSWORD_JSON_PATH, hasItem(INVALID_RESPONSE));
    }

    private String getRedirectionUrl() {
        String location = given()
                .contentType(ContentType.JSON)
                .when()
                .redirects().follow(false)
                .post(FULL_URL)
                .header(LOCATION_HEADER_NAME);
        return location;
    }
}
