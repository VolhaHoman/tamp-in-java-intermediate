package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.ApiUserDTO;
import com.epam.mentoring.taf.api.ResponseDTO;
import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.ui.page.LoginPage;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserSignUpTest extends AbstractTest {

    private UserDataDTO userDataDTO;
    private UserDataDTO defaultUserData;

    @BeforeMethod
    public void generateUserData() {
        userDataDTO = UserData.generateUserData();
        defaultUserData = UserData.getDefaultUserData();
    }

    @Test
    public void signUpVerification() {
        LoginPage loginPage = new LoginPage(baseUrl);
        loginPage.clickSignUpLink()
                .fillInUsername(userDataDTO.getUserName())
                .fillInEmail(userDataDTO.getUserEmail())
                .fillInPassword(userDataDTO.getUserPassword())
                .clickSignUpBtn();

        HomePage homePage = new HomePage();
        Assert.assertEquals(homePage.getUsernameAccountNav(), userDataDTO.getUserName());
    }

    @Test
    public void apiRegisterVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(userDataDTO.getUserEmail(), userDataDTO.getUserPassword())
                .setUsername(userDataDTO.getUserName())
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void apiAlreadyRegisteredUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .setUsername(defaultUserData.getUserName())
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response);
        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), "has already been taken");
    }

    @Test
    public void apiBlankUserVerification() {
        ApiUserDTO apiUserDTO = new ApiUserDTO
                .ApiUserDTOBuilder(defaultUserData.getUserEmail(), defaultUserData.getUserPassword())
                .setUsername("")
                .build();
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendApiRequest(apiUserDTO);
        ResponseDTO responseDTO = restAPIClient.transformToDto(response);
        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(responseDTO.getErrors().getUsername().get(0), "can't be blank");
    }
}
