package com.sasho.demo;

import com.sasho.demo.controller.model.request.AddNewAddress;
import com.sasho.demo.controller.model.request.UpdateAddressRequest;
import com.sasho.demo.repository.AddressRepo;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAddress extends BaseTestClass {
    @SpyBean
    private AddressRepo addressRepo;

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

    @Test
    @Order(2)
    public void test_Update_Address() {
        var request = UpdateAddressRequest.builder().street("cc").city("dd").addressId(1L).build();
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/address")
                .then()
                .statusCode(200);

        var address = addressRepo.findById(1l).get();
        assertEquals("cc", address.getStreet());
        assertEquals("dd", address.getCity());
    }

    @Test
    @Order(3)
    public void test_Delete_Address() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/address/{id}")
                .then()
                .statusCode(200);

        var all = this.addressRepo.findAll();
        assertTrue(all.isEmpty());
    }
}
