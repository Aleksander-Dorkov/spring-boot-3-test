package com.sasho.demo;

import com.sasho.demo.configuration.security.Authorities;
import com.sasho.demo.controller.model.request.RegisterUserRequest;
import com.sasho.demo.domain.Authority;
import com.sasho.demo.repository.AuthorityRepo;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoApplicationTests {
    @LocalServerPort
    private int port;
    @Autowired
    private AuthorityRepo authorityRepo;

    @BeforeAll
    public void setUp() {
        RestAssured.port = port;
        RestAssured.filters(new Filter() {
            @Override
            public Response filter(FilterableRequestSpecification requestSpec,
                                   FilterableResponseSpecification responseSpec, FilterContext ctx) {
                String csrfToken = requestSpec.getCookies().getValue("XSRF-TOKEN");
                if (csrfToken == null) {
                    csrfToken = RestAssured.given().noFilters().get("/").cookie("XSRF-TOKEN");
                }
                requestSpec.replaceHeader("X-XSRF-TOKEN", csrfToken);
                return ctx.next(requestSpec, responseSpec);
            }
        });
        this.authorityRepo.deleteAll();
        var a1 = Authority.builder().authority(Authorities.ROLE_ADMIN.name()).build();
        var a2 = Authority.builder().authority(Authorities.ROLE_USER.name()).build();
        this.authorityRepo.saveAll(Set.of(a1, a2));
    }

    @Test
    @Order(1)
    void contextLoads() {
        int size = this.authorityRepo.findAll().size();
        Assertions.assertEquals(2, size);
    }

    @Test
    @Order(2)
    public void testRegisterUser() {
        RegisterUserRequest request = new RegisterUserRequest("testuser", "password123");
        String responseBody = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users/register")
                .then()
                .statusCode(200) // Assuming success returns a 200 status code
                .extract()
                .response()
                .asString();
        Assertions.assertEquals("success", responseBody);
    }

    @AfterAll
    public void tearDown() {
        this.authorityRepo.deleteAll();
    }
}
