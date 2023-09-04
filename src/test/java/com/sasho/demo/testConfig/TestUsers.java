package com.sasho.demo.testConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TestUsers {
    ALL_AUTHORITIES_USER(1L, "sasho", "1234"),
    STANDARD_USER(2L, "user", "1234"),
    ADMIN_USER(3L, "admin", "1234");
    private final long id;
    private final String username;
    private final String password;

    public long id() {
        return this.id;
    }

    public String userName() {
        return this.username;
    }

    public String password() {
        return this.password;
    }
}
