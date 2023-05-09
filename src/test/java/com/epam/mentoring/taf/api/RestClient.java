package com.epam.mentoring.taf.api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class RestClient {

    private Logger log;

    public RestClient(Logger log) {
        this.log = log;
    }

    @Step("Send GET API request to endpoint")
    public Response sendGetRequest(String path,
                                   Map<String, String> headers,
                                   Map<String, String> params) {
        Response result = getRequestSpecification(headers, params)
                .get(path)
                .then()
                .extract().response();

        log.info("Request path: " + path);
        log.info("Response status: " + result.getStatusCode());
        log.info("Response body: " + result.getBody().asString());
        return result;
    }

    @Step("Send POST API request to endpoint")
    public Response sendPostRequest(String path, String body,
                                   Map<String, String> headers,
                                   Map<String, String> params) {
        Response result = getRequestSpecification(headers, params)
                .body(body)
                .post(path)
                .then()
                .extract().response();

        log.info("Request body: " + body);
        log.info("Request path: " + path);

        log.info("Response status: " + result.getStatusCode());
        log.info("Response body: " + result.getBody().asString());
        return result;
    }

    @Step("Send PUT API request to endpoint")
    public Response sendPutRequest(String path, String body,
                                    Map<String, String> headers) {
        Response result = getRequestSpecification(headers)
                .body(body)
                .put(path)
                .then()
                .extract().response();

        log.info("Request body: " + body);
        log.info("Request path: " + path);

        log.info("Response status: " + result.getStatusCode());
        log.info("Response body: " + result.getBody().asString());
        return result;
    }

    @Step("Send DELETE API request to endpoint")
    public Response sendDeleteRequest(String path, String body,
                                      Map<String, String> headers,
                                      Map<String, String> params) {
        Response result = getRequestSpecification(headers, params)
                .body(body)
                .delete(path)
                .then()
                .extract().response();
        log.info("Request body: " + body);
        log.info("Request path: " + path);

        log.info("Response status: " + result.getStatusCode());
        log.info("Response body: " + result.getBody().asString());
        return result;
    }

    private RequestSpecification prepareRequest() {
        RequestSpecification response = given()
                .when()
                .contentType(ContentType.JSON);
        return response;
    }

    private RequestSpecification getRequestSpecification(Map<String, String> headers, Map<String, String> params) {
        RequestSpecification response = prepareRequest();
        fillHeaders(headers, response);
        fillParams(params, response);
        return response;
    }

    private RequestSpecification getRequestSpecification(Map<String, String> headers) {
        RequestSpecification response = prepareRequest();
        fillHeaders(headers, response);
        return response;
    }

    private void fillHeaders(Map<String, String> headers, RequestSpecification response) {
        if (!headers.isEmpty()) {
            response.headers(headers);
            log.info("Request headers: {}", headers);
        }
    }

    private void fillParams(Map<String, String> params, RequestSpecification response) {
        if (!params.isEmpty()) {
            response.params(params);
            log.info("Request params: {}", params);
        }
    }

    public Response sendGetRequest(String path) {
        return sendGetRequest(path, Map.of(), Map.of());
    }

    public Response sendGetRequestWithHeaders(String path, Map<String, String> headers) {
        return sendGetRequest(path, headers, Map.of());
    }

    public Response sendGetRequestWithParams(String path, Map<String, String> params) {
        return sendGetRequest(path, Map.of(), params);
    }

    public Response sendPostRequest(String path, String body) {
        return sendPostRequest(path, body, Map.of(), Map.of());
    }

    public Response sendPostRequestWithHeaders(String path, String body, Map<String, String> headers) {
        return sendPostRequest(path, body, headers, Map.of());
    }

    public Response sendPostRequestWithParams(String path, String body, Map<String, String> params) {
        return sendPostRequest(path, body, Map.of(), params);
    }

    public Response sendPutRequestWithHeaders(String path, String body, Map<String, String> headers) {
        return sendPutRequest(path, body, headers);
    }

    public Response sendDeleteRequestWithHeaders(String path, String body, Map<String, String> headers) {
        return sendDeleteRequest(path, body, headers, Map.of());
    }
}
