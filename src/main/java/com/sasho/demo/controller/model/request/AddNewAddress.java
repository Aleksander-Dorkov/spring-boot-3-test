package com.sasho.demo.controller.model.request;

import lombok.Builder;

@Builder
public record AddNewAddress(Long userId, String street, String city) {
}
