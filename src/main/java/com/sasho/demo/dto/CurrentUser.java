package com.sasho.demo.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record CurrentUser(Long id,
                          String username,
                          Set<String> authorities) {
}
