package com.soupapp.soup.controllers;

import com.soupapp.soup.dtos.user.LoginMessage;
import com.soupapp.soup.dtos.user.UserLoginDto;
import com.soupapp.soup.dtos.user.UserResponseDto;
import com.soupapp.soup.dtos.user.UserRegisterDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.soupapp.soup.services.UserService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( "/api/Users")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //fara verbe /Users/list
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDto>> getAll(){
        try{
            List<UserResponseDto> usersList = new ArrayList<UserResponseDto>();
            usersList = userService.getAll();
            if(usersList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRegisterDto request) throws URISyntaxException {
        var response = userService.create(request);
        if (response != null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(new URI(String.format("/users/%s", response.getId())));
            return new ResponseEntity<>(response, responseHeaders, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto request)
    {
        LoginMessage loginResponse = userService.login(request);
        return ResponseEntity.ok(loginResponse);
    }
}
