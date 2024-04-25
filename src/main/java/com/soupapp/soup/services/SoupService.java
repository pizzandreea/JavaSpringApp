package com.soupapp.soup.services;

import com.soupapp.soup.dtos.SoupCreateDto;
import com.soupapp.soup.dtos.user.UserResponseDto;
import com.soupapp.soup.models.Soup;
import com.soupapp.soup.models.SoupType;
import com.soupapp.soup.models.User;
import com.soupapp.soup.repositories.SoupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SoupService {
    private final SoupRepository soupRepository;

    public SoupService(SoupRepository soupRepository){ this.soupRepository = soupRepository;}

    public Integer create(SoupCreateDto request){
        var soups = soupRepository.findAll();
        if(soups.stream().noneMatch(x -> x.getName().equals(request.getName()))){
            Soup soup = request.toSoup(new Soup());
            var createdsoup = soupRepository.save(soup);
            return createdsoup.getId();
        }
        return null;
    }
    public List<Soup> getAll(){
        var soups = soupRepository.findAll();
        return soups;
    }
    public List<Soup> getSoupsByType(SoupType type) {
        return soupRepository.findByType(type);
    }

    public List<Soup> filterSoupsByIngredients(List<String> ingredients) {
        return soupRepository.findByIngredients(ingredients);
    }
    public Soup restockSoupById(Integer soupId, int stock) {
        Optional<Soup> optionalSoup = soupRepository.findById(soupId);

        if (optionalSoup.isPresent()) {
            Soup soup = optionalSoup.get();
            int updatedStock = soup.getStock() + stock;
            soup.setStock(updatedStock);
            Soup updatedSoup = soupRepository.save(soup);
            return updatedSoup;
        } else {
            throw new IllegalArgumentException("Soup with ID " + soupId + " not found");
        }
    }



}
