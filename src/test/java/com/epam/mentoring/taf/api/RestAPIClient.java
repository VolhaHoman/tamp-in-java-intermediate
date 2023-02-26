package com.epam.mentoring.taf.api;

import com.epam.mentoring.taf.AbstractTest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RestAPIClient {

    public static final String API_USERS = "https://api.realworld.io/api/users";
    private static final String ARTICLES_BY_TAG_URL = "/api/articles?tag={tag}&limit=10&offset=0";

    @Step("Send API request to endpoint")
    public Response sendApiRequest(ApiUserDTO apiUserDTO) {

        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .body(apiUserDTO.toString())
                .post(API_USERS)
                .then()
                .extract().response();
        return response;
    }

    @Step("Transform API response to DTO")
    public ResponseDTO transformToDto(Response response) {
        ResponseDTO responseDTO = response.body().as(ResponseDTO.class);
        return responseDTO;
    }

    @Step("Send Get request for the given tag")
    public Response sendGetTagRequest(String tag) {
        Response response = given()
                .baseUri(AbstractTest.API_URL)
                .when()
                .get(ARTICLES_BY_TAG_URL, tag);
        //        logger.info("Request sent for: " + tag);
        //        logger.info("Response status: " + response.getStatusCode());
        return response;
    }
}
