package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.ApiUserDTO;
import com.epam.mentoring.taf.api.ResponseDTO;
import com.epam.mentoring.taf.api.RestAPIClient;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

public class UserSignUpTest extends AbstractTest {

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

        driver.get(UI_URL);
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
        ApiUserDTO apiUserDTO = new ApiUserDTO.ApiUserDTOBuilder(email, password).setUsername(username).build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        Assert.assertEquals(response.getStatusCode(), 200);
}

    @Test
    public void apiAlreadyRegisteredUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO.ApiUserDTOBuilder(email, password).setUsername(username).build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response);
        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), "has already been taken");
    }

    @Test
    public void apiBlankUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO.ApiUserDTOBuilder(email, password).setUsername("").build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response);
        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), "can't be blank");
    }

}
