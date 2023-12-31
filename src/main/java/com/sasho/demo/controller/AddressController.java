package com.sasho.demo.controller;

import com.sasho.demo.controller.model.request.AddNewAddress;
import com.sasho.demo.controller.model.request.UpdateAddressRequest;
import com.sasho.demo.controller.model.response.EmptyResponse;
import com.sasho.demo.domain.Address;
import com.sasho.demo.repository.AddressRepo;
import com.sasho.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final UserRepo userRepo;
    private final AddressRepo addressRepo;

    @PostMapping("/address")
    public EmptyResponse addAddress(@RequestBody AddNewAddress request) {
        var user = this.userRepo.findDomainUserById(request.userId());
        var address = Address.builder().street(request.street()).city(request.city()).build();
        user.addAddress(address);
        this.userRepo.save(user);
        return new EmptyResponse("success");
    }

    @PutMapping("/address")
    public EmptyResponse updateAddress(@RequestBody UpdateAddressRequest request) {
        var address = this.addressRepo.findById(request.addressId()).get();
        address.updateAddress(request);
        this.addressRepo.save(address);
        return new EmptyResponse("success");
    }

    @DeleteMapping("/address/{addressId}")
    public EmptyResponse deleteAddress(@PathVariable Long addressId) {
        this.addressRepo.deleteById(addressId);
        return new EmptyResponse("success");
    }
}
