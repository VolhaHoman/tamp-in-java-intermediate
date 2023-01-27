package com.epam.mentoring.taf;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

public class UserSignUpTest extends AbstractTest {
    public static final String SIGNING_URL = "/api/users";
    public static final String FULL_URL = API_URL + SIGNING_URL;
    public static final String LOCATION_HEADER_NAME = "Location";
    public static final String JSON_BODY = "{\"user\":{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}}";
    public static final String URL_REG = "https://angular.realworld.io/register";
    private final String username = "Test User";
    private final String email = "test_user@example.com";
    private final String password = "test_password";

    @Test
    public void signUpVerification() {
        String uniqueId = RandomStringUtils.randomNumeric(1000);
        String username = this.username + uniqueId;
        String email = this.email.replace("@", "." + uniqueId + "@");

        driver.get(baseUrl);
        driver.findElement(By.xpath("//li/a[contains(text(),'Sign up')]")).click();
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(text(),'Sign up')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class,'navbar-nav')]/li[4]/a")));

        String actualUserName = driver.findElement(By.xpath("//ul[contains(@class,'navbar-nav')]/li[4]/a")).getText();
        Assert.assertEquals(actualUserName, username);

    }

    @Test
    public void apiRegisterVerification() {
        String uniqueId = RandomStringUtils.randomNumeric(1000);
        String username = this.username + uniqueId;
        String email = this.email.replace("@", "." + uniqueId + "@");

        given()
                .when()
                .baseUri(URL_REG)
                .contentType(ContentType.JSON)
                .body(String.format(JSON_BODY, email, password, username))
                .post(getRedirectionUrl())
                .then()
                .statusCode(200);
    }

    @Test
    public void apiAlreadyRegisteredUserVerification() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(String.format(JSON_BODY, email, password, username))
                .post(getRedirectionUrl())
                .then()
                .statusCode(422)
                .body("errors.username", hasItem("has already been taken"));
    }

    @Test
    public void apiBlankUserVerification() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(String.format(JSON_BODY, email, password, ""))
                .post(getRedirectionUrl())
                .then()
                .statusCode(422)
                .body("errors.username", hasItem("can't be blank"));
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
