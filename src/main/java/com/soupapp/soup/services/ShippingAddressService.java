package com.soupapp.soup.services;


import com.soupapp.soup.dtos.ShippingAddressDto;
import com.soupapp.soup.exceptions.EntityNotFoundException;
import com.soupapp.soup.models.ShippingAddress;
import com.soupapp.soup.models.User;
import com.soupapp.soup.repositories.ShippingAddressRepository;
import com.soupapp.soup.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserRepository userRepository;

    public ShippingAddressService(ShippingAddressRepository shippingAddressRepository,
                                  UserRepository userRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
        this.userRepository = userRepository;
    }

    public ShippingAddress addAddressForUser(User user, ShippingAddress newAddress) {
        newAddress.setUser(user);
        return shippingAddressRepository.save(newAddress);
    }
    public ShippingAddress updateShippingAddress(Integer userId, Integer addressId, ShippingAddressDto updatedAddressDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        ShippingAddress shippingAddress = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Shipping address with ID " + addressId + " not found"));

        shippingAddress.setStreet(updatedAddressDto.getStreet());
        shippingAddress.setCity(updatedAddressDto.getCity());
        shippingAddress.setPostalCode(updatedAddressDto.getPostalCode());

        return shippingAddressRepository.save(shippingAddress);
    }

}