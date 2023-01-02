package com.epam.mentoring.taf;

import com.epam.mentoring.taf.page.HomePage;
import com.epam.mentoring.taf.page.LoginPage;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.epam.mentoring.taf.data.UserData.*;
import static com.epam.mentoring.taf.page.HomePage.USERNAME_ACCOUNT_NAV;
import static com.epam.mentoring.taf.page.LoginPage.CREDENTIALS_ERROR_TEXT;
import static com.epam.mentoring.taf.page.LoginPage.INVALID_CREDENTIALS_MESSAGE;
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
        signIn(DEFAULT_PASSWORD);
        HomePage homePage = new HomePage(driver, wait);
        Assert.assertEquals(homePage.getTextWithWait(USERNAME_ACCOUNT_NAV), DEFAULT_USERNAME);
    }

    @Test
    public void uiSignInWithInvalidCredentialsVerification() {
        signIn(WRONG_PASSWORD);
        HomePage homePage = new HomePage(driver, wait);
        Assert.assertEquals(homePage.getTextWithWait(INVALID_CREDENTIALS_MESSAGE), CREDENTIALS_ERROR_TEXT);
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

    private void signIn(String wrongPassword) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.signIn(wrongPassword);
    }
}
