package com.sasho.demo;

import com.sasho.demo.controller.model.request.AddNewAddress;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestAddress extends BaseTestClass {
    @BeforeAll
    public void setUp() {
        super.logInUserWithAllAuthorities();
    }

    @Test
    @Order(1)
    public void test_Add_Address() {
        var request = AddNewAddress.builder().street("aa").city("bb").userId(1L).build();
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/address")
                .then()
                .statusCode(200);
    }
}
