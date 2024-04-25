package com.soupapp.soup.repositories;

import com.soupapp.soup.models.Soup;
import com.soupapp.soup.models.SoupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoupRepository extends JpaRepository<Soup, Integer> {
    List<Soup> findByType(SoupType type);
    @Query("SELECT DISTINCT s FROM Soup s JOIN s.ingredients i WHERE i IN :ingredients")
    List<Soup> findByIngredients(@Param("ingredients") List<String> ingredients);



}
