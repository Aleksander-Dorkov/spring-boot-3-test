package com.sasho.demo.controller;

import com.sasho.demo.controller.model.request.AddNewAddress;
import com.sasho.demo.domain.Address;
import com.sasho.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final UserRepo userRepo;

    @PostMapping("/address")
    public String addAddress(@RequestBody AddNewAddress request) {
        var user = this.userRepo.findDomainUserById(request.userId());
        var address = Address.builder().street(request.street()).city(request.city()).build();
        user.addAddress(address);
        this.userRepo.save(user);
        return "success";
    }
}
