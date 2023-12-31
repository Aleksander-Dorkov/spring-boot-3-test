package com.sasho.demo;

import com.sasho.demo.controller.model.request.RegisterUserRequest;
import com.sasho.demo.repository.AddressRepo;
import com.sasho.demo.repository.UserRepo;
import com.sasho.demo.testConfig.TestUsers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseTestClass {
    @SpyBean
    protected AddressRepo addressRepo;
    @SpyBean
    protected UserRepo userRepo;

    @LocalServerPort
    private int port;
    protected String csrfToken;
    protected String jwtToken;

    @BeforeAll
    public void initialTestSetUp() {
        setUpRestAssured();
    }


    private void setUpRestAssured() {
        RestAssured.port = port;
        this.csrfToken = given().noFilters().get("/csrf").cookie("XSRF-TOKEN");
        RestAssured.filters((requestSpec, responseSpec, ctx) -> {
            Map<String, String> headers = Map.of("X-XSRF-TOKEN", csrfToken);
            Map<String, String> cookies = Map.of("XSRF-TOKEN", csrfToken, "JWT", jwtToken == null ? "" : jwtToken);

            requestSpec.headers(headers);
            requestSpec.cookies(cookies);

            return ctx.next(requestSpec, responseSpec);
        });
    }

    protected void logInUserWithAllAuthorities() {
        var request = new RegisterUserRequest(TestUsers.ALL_AUTHORITIES_USER.userName(), TestUsers.ALL_AUTHORITIES_USER.password());
        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users/login")
                .then()
                .statusCode(200)
                .extract()
                .response();
        jwtToken = response.getCookie("JWT");
    }
}
