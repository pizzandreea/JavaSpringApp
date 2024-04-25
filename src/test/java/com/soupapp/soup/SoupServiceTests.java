package com.soupapp.soup;

import com.soupapp.soup.dtos.SoupCreateDto;
import com.soupapp.soup.models.Soup;
import com.soupapp.soup.models.SoupType;
import com.soupapp.soup.repositories.SoupRepository;
import com.soupapp.soup.services.SoupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SoupServiceTests {

    private SoupRepository soupMockRepository;
    private SoupService soupService;

    @BeforeEach
    void init(){
        soupMockRepository = mock(SoupRepository.class);
        soupService = new SoupService(soupMockRepository);
    }
    @Test
    void createSoup() {
        SoupCreateDto request = new SoupCreateDto();
        request.setName("Chicken Soup");

        Soup createdSoup = new Soup();
        createdSoup.setId(1);
        createdSoup.setName(request.getName());

        when(soupMockRepository.findAll()).thenReturn(List.of());
        when(soupMockRepository.save(any(Soup.class))).thenReturn(createdSoup);

        Integer soupId = soupService.create(request);

        assertEquals(1, soupId);

        verify(soupMockRepository, times(1)).save(any(Soup.class));
    }
    @Test
    void getAllSoups() {
        List<Soup> soups = List.of(new Soup(), new Soup());

        when(soupMockRepository.findAll()).thenReturn(soups);

        List<Soup> response = soupService.getAll();

        assertEquals(soups.size(), response.size());

        verify(soupMockRepository, times(1)).findAll();
    }


    @Test
    void createDuplicateSoup() {
        SoupCreateDto request = new SoupCreateDto();
        request.setName("Chicken Soup");

        Soup existingSoup = new Soup();
        existingSoup.setId(1);
        existingSoup.setName(request.getName());

        when(soupMockRepository.findAll()).thenReturn(List.of(existingSoup));

        Integer soupId = soupService.create(request);

        assertEquals(null, soupId);

        verify(soupMockRepository, times(0)).save(any(Soup.class));
    }

    @Test
    void getSoupsByType() {
        SoupType soupType = SoupType.VEGETARIAN;
        List<Soup> soups = List.of(new Soup(), new Soup());

        when(soupMockRepository.findByType(soupType)).thenReturn(soups);

        List<Soup> response = soupService.getSoupsByType(soupType);

        assertEquals(soups.size(), response.size());

        verify(soupMockRepository, times(1)).findByType(soupType);
    }


    @Test
    void filterSoupsByIngredients() {
        List<String> ingredients = List.of("Carrots", "Chicken", "Noodles");
        List<Soup> soups = List.of(new Soup(), new Soup());

        when(soupMockRepository.findByIngredients(ingredients)).thenReturn(soups);

        List<Soup> response = soupService.filterSoupsByIngredients(ingredients);

        assertEquals(soups.size(), response.size());

        verify(soupMockRepository, times(1)).findByIngredients(ingredients);
    }


    @Test
    void restockSoupById() {
        Integer soupId = 1;
        int stockToAdd = 5;

        Soup existingSoup = new Soup();
        existingSoup.setId(soupId);
        existingSoup.setStock(10);

        when(soupMockRepository.findById(soupId)).thenReturn(Optional.of(existingSoup));
        // Adjust the save behavior to update the existingSoup in place
        when(soupMockRepository.save(any(Soup.class))).thenReturn(existingSoup);


        Soup updatedSoup = soupService.restockSoupById(soupId, stockToAdd);

        assertNotNull(updatedSoup);
        assertEquals(10 + stockToAdd, updatedSoup.getStock());


        verify(soupMockRepository, times(1)).findById(soupId);
        // Verify that save is called with the same instance
        verify(soupMockRepository, times(1)).save(existingSoup);
    }




}
