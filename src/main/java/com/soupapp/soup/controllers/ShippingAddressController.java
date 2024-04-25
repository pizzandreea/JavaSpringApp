package com.soupapp.soup.controllers;

import com.soupapp.soup.dtos.ShippingAddressDto;
import com.soupapp.soup.models.ShippingAddress;
import com.soupapp.soup.models.User;
import com.soupapp.soup.repositories.UserRepository;
import com.soupapp.soup.services.ShippingAddressService;
import com.soupapp.soup.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Users/{userId}/Addresses")
@CrossOrigin
public class ShippingAddressController {

    private final UserService userService;
    private final ShippingAddressService shippingAddressService;

    public ShippingAddressController(UserService userService, ShippingAddressService shippingAddressService) {
        this.userService = userService;
        this.shippingAddressService = shippingAddressService;
    }

    @PostMapping("/createAddress")
    public ResponseEntity<User> addShippingAddress(@PathVariable Integer userId, @RequestBody @Valid ShippingAddressDto addressDto) {

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ShippingAddress newAddress = new ShippingAddress();
        newAddress.setStreet(addressDto.getStreet());
        newAddress.setCity(addressDto.getCity());
        newAddress.setPostalCode(addressDto.getPostalCode());

        user.getShippingAddresses().add(newAddress);
        newAddress.setUser(user);

        userService.saveUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/updateAddress/{addressId}")
    public ResponseEntity<ShippingAddress> updateShippingAddress(
            @PathVariable Integer userId,
            @PathVariable Integer addressId,
            @RequestBody ShippingAddressDto updatedAddressDto) {
        var newAddress = shippingAddressService.updateShippingAddress(userId, addressId, updatedAddressDto);
        return new ResponseEntity<>(newAddress, HttpStatus.OK);
    }
}