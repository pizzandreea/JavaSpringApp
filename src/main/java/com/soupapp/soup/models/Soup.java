package com.soupapp.soup.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


@Entity
@Table(name = "soups")
public class Soup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String name;
    private double price;
    @ElementCollection
    @CollectionTable(name = "soup_ingredients", joinColumns = @JoinColumn(name = "soup_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;
    @Enumerated(EnumType.STRING)
    private SoupType type;
    @Min(0)
    private int stock;

    @OneToMany(mappedBy = "soup")
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "soup", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviews;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public SoupType getType() {
        return type;
    }

    public void setType(SoupType type) {
        this.type = type;
    }
}
