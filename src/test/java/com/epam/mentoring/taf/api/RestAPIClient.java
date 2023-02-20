package com.epam.mentoring.taf.api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class RestAPIClient {

    public final Logger logger = LogManager.getRootLogger();

    @Step("Send API request to endpoint")
    public Response sendApiRequest(ApiUserDTO apiUserDTO, String url) {

        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .body(apiUserDTO.toString())
                .post(url)
                .then()
                .extract().response();
        logger.info("Request body: " + apiUserDTO);
        logger.info("Response status: " + response.getStatusCode());
        return response;
    }

    @Step("Transform API response to DTO")
    public ResponseDTO transformToDto(Response response) {
        ResponseDTO responseDTO = response.body().as(ResponseDTO.class);
        logger.info("Response message: " + responseDTO.getErrors().getUsername().get(0));
        return responseDTO;
    }

}
