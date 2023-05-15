package com.epam.mentoring.taf.util;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class Redirection {

    public static final String LOCATION_HEADER_NAME = "Location";

    private Redirection() {
    }

    public static String getRedirectionUrl(String url) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .redirects().follow(false)
                .post(url)
                .header(LOCATION_HEADER_NAME);
    }
}
