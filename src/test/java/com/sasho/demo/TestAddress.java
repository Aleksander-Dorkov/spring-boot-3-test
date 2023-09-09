package com.sasho.demo;

import com.sasho.demo.controller.model.request.AddNewAddress;
import com.sasho.demo.controller.model.request.UpdateAddressRequest;
import com.sasho.demo.controller.model.response.EmptyResponse;
import com.sasho.demo.testConfig.TestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAddress extends BaseTestClass {

    @BeforeAll
    public void setUp() {
        super.logInUserWithAllAuthorities();
    }

    @Test
    @Order(1)
    public void test_Add_Address() {
        var request = AddNewAddress.builder().street("aa").city("bb").userId(1L).build();
        var resp = TestClient.post("/address", request, HttpStatus.OK, EmptyResponse.class);
    }

    @Test()
    @Order(2)
    public void test_Update_Address() {
        var request = UpdateAddressRequest.builder().street("cc").city("dd").addressId(1L).build();
        var resp = TestClient.put("/address", request, HttpStatus.OK, EmptyResponse.class);

        var address = addressRepo.findById(1L).get();
        assertEquals("cc", address.getStreet());
        assertEquals("dd", address.getCity());
    }

    @Test
    @Order(3)
    public void test_Delete_Address() {
        var resp = TestClient.delete("/address/1", HttpStatus.OK, EmptyResponse.class);
        var all = this.addressRepo.findAll();
        assertTrue(all.isEmpty());
    }
}
