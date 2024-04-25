package com.soupapp.soup.controllers;

import com.soupapp.soup.dtos.SoupCreateDto;
import com.soupapp.soup.dtos.user.UserResponseDto;
import com.soupapp.soup.models.Soup;
import com.soupapp.soup.models.SoupType;
import com.soupapp.soup.services.SoupService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( "/api/Soups")
@CrossOrigin
public class SoupController {
    private final SoupService soupService;

    public SoupController(SoupService soupService){this.soupService = soupService;}
    @PostMapping("/createSoup")
    public ResponseEntity<Integer> create(@RequestBody SoupCreateDto request) throws URISyntaxException {
        Integer response = soupService.create(request);

        if (response != null) {
            return ResponseEntity.created(new URI("/soups/" + response)).body(response);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getAllSoups")
    public ResponseEntity<List<Soup>> getAll(){
        try{
            List<Soup> soupsList = new ArrayList<Soup>();
            soupsList = soupService.getAll();
            if(soupsList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(soupsList, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getSoupsByType/{type}")
    public ResponseEntity<List<Soup>> getSoupsByType(@PathVariable SoupType type) {
        try {
            List<Soup> soupsList = soupService.getSoupsByType(type);
            if (soupsList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(soupsList, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getSoupsWithIngredients")
    public ResponseEntity<List<Soup>> filterByIngredients(@RequestParam List<String> ingredients) {
        List<Soup> soups = soupService.filterSoupsByIngredients(ingredients);
        if (soups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(soups, HttpStatus.OK);
    }
    @PostMapping("/restockSoup/{id}")
    public ResponseEntity<Soup> restockSoup(@PathVariable Integer id, @RequestParam int stock) {
        try {
            Soup updatedSoup = soupService.restockSoupById(id, stock);
            return new ResponseEntity<>(updatedSoup, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
