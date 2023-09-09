package com.sasho.demo.controller.model.request;

import lombok.Builder;

@Builder
public record UpdateAddressRequest(Long addressId, String street, String city) {
}
