package com.epam.mentoring.taf.api;

import com.epam.mentoring.taf.AbstractTest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
    public SignInResponseDTO transformToDto(Response response, Logger logger) {
        SignInResponseDTO signInResponseDTO = response.body().as(SignInResponseDTO.class);
        logger.info("Response message: " + signInResponseDTO.getErrors().getUsername().get(0));
        return signInResponseDTO;
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

    @Step("Transform API response to DTO for comment")
    public CommentDTO transformToDtoCom(Response response) {
        CommentDTO commentDTO = response.body().as(CommentDTO.class);
        return commentDTO;
    }

    public ArticleResponseDTO transformToDtoArticle(Response response, Logger logger) {
        ArticleResponseDTO articleResponseDTO = response.body().as(ArticleResponseDTO.class);
        logger.info("Response message: " + articleResponseDTO.getArticle());
        return articleResponseDTO;
    }
}
