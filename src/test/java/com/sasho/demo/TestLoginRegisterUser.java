package com.sasho.demo;

import com.sasho.demo.controller.model.request.RegisterUserRequest;
import com.sasho.demo.repository.UserRepo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestLoginRegisterUser extends BaseTestClass {
    @Autowired
    private UserRepo userRepo;

    @Test
    @Order(1)
    public void test_Register_User() {
        RegisterUserRequest request = new RegisterUserRequest("testuser", "password123");
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users/register")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    public void test_Register_User_Duplicate_Username() {
        RegisterUserRequest request = new RegisterUserRequest("testuser", "password123");
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users/register")
                .then()
                .statusCode(403);
    }

    @Test
    @Order(3)
    public void test_Login_User_Wrong_Username() {
        RegisterUserRequest invalidUsername = new RegisterUserRequest("wrongUsername", "password123");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(invalidUsername)
                .when()
                .post("/users/login")
                .then()
                .statusCode(403)
                .extract()
                .response();
        String jwtCookieValue = response.getCookie("JWT");
        Assertions.assertNull(jwtCookieValue);
    }

    @Test
    @Order(4)
    public void test_Login_User_Wrong_Password() {
        RegisterUserRequest invalidPassword = new RegisterUserRequest("testuser", "wrongPassword");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(invalidPassword)
                .when()
                .post("/users/login")
                .then()
                .statusCode(401)
                .extract()
                .response();
        String jwtCookieValue = response.getCookie("JWT");
        Assertions.assertNull(jwtCookieValue);
    }

    @Test
    @Order(5)
    public void test_Login_User_Susses() {
        RegisterUserRequest request = new RegisterUserRequest("testuser", "password123");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users/login")
                .then()
                .statusCode(200)
                .extract()
                .response();
        String jwtCookieValue = response.getCookie("JWT");
        super.jwtToken = jwtCookieValue;
        Assertions.assertNotNull(jwtCookieValue);
    }
}
