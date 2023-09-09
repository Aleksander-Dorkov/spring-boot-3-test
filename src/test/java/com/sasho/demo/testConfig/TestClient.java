package com.sasho.demo.testConfig;

import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class TestClient {

    public static <T> T post(String url, Object request, HttpStatus expectedStatus, Class<T> responseClass) {
        if (responseClass == Void.class) {
            given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post(url)
                    .then()
                    .statusCode(expectedStatus.value());
            return null;
        }
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(url)
                .then()
                .statusCode(expectedStatus.value())
                .extract()
                .body()
                .as(responseClass);
    }

    public static <T> T put(String url, Object request, HttpStatus expectedStatus, Class<T> responseClass) {
        if (responseClass == Void.class) {
            given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .put(url)
                    .then()
                    .statusCode(expectedStatus.value());
            return null;
        }
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put(url)
                .then()
                .statusCode(expectedStatus.value())
                .extract()
                .body()
                .as(responseClass);
    }

    public static <T> T delete(String url, HttpStatus expectedStatus, Class<T> responseClass) {
        if (responseClass == Void.class) {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(url)
                    .then()
                    .statusCode(expectedStatus.value());
            return null;
        }
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then()
                .statusCode(expectedStatus.value())
                .extract()
                .body()
                .as(responseClass);
    }
}
