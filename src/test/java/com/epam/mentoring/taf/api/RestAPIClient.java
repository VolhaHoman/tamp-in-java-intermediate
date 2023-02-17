package com.epam.mentoring.taf.api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RestAPIClient {

    public static final String API_USERS = "https://api.realworld.io/api/users";

    @Step("Send API request to endpoint")
    public Response sendApiRequest(ApiUserDTO apiUserDTO, String url) {

        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .body(apiUserDTO.toString())
                .post(url)
                .then()
                .extract().response();
        return response;
    }

    @Step("Transform API response to DTO")
    public ResponseDTO transformToDto(Response response) {
        ResponseDTO responseDTO = response.body().as(ResponseDTO.class);
        return responseDTO;
    }

}
