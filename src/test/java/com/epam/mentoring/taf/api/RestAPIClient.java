package com.epam.mentoring.taf.api;

import com.epam.mentoring.taf.dataobject.ApiUserDTO;
import com.epam.mentoring.taf.dataobject.ResponseDTO;
import com.epam.mentoring.taf.exception.ResponseCheckException;
import com.epam.mentoring.taf.tests.AbstractTest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class RestAPIClient {

    private static final String ARTICLES_BY_TAG_URL = "/api/articles?tag={tag}&limit=10&offset=0";

    public final Logger logger = LogManager.getRootLogger();

    @Step("Send API request to endpoint")
    public Response sendApiRequest(ApiUserDTO apiUserDTO, String url, Logger logger) {
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
    public ResponseDTO transformToDto(Response response, Logger logger) {
        ResponseDTO responseDTO = extractResponse(response);
        logUserName(responseDTO);
        return responseDTO;
    }

    @Step("Send Get request for the given tag")
    public Response sendGetTagRequest(String tag, Logger logger) {
        Response response = given()
                .baseUri(AbstractTest.API_URL)
                .when()
                .get(ARTICLES_BY_TAG_URL, tag);
        logger.info("Request sent for: " + tag);
        logger.info("Response status: " + response.getStatusCode());
        return response;
    }

    private ResponseDTO extractResponse(Response response) {
        checkResponse(response);
        try {
            return response.body().as(ResponseDTO.class);
        } catch (Exception e) {
            throw new ParseException("Can't parse body");
        }
    }

    private void checkResponse(Response response) {
        if (ObjectUtils.anyNull(response, response.body()) || !is2xxStatus(response)) {
            throw new ResponseCheckException("Response error");
        }
    }

    private boolean is2xxStatus(Response response) {
        return response.getStatusCode() >= 200 && response.getStatusCode() < 300;
    }

    private void logUserName(ResponseDTO responseDTO) {
        logger.info("Response message: " + extractUserName(responseDTO));
    }

    private String extractUserName(ResponseDTO dto) {
        if (dto.getErrors() != null || !dto.getErrors().getUsername().isEmpty()) {
            return dto.getErrors().getUsername().get(0);
        }
        return "UNKNOWN USER";
    }

}
