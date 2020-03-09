package com.example.countryservices;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class
Country {
    @Id
    @GeneratedValue
    Long id;
    String name;
    String region;
    int population;
    int gdpPerCapita;


    public Country(Long id, String name, String region, int population, int gdpPerCapita) {
        this.name = name;
        this.region = region;
        this.population = population;
        this.id = id;
        this.gdpPerCapita = gdpPerCapita;
    }
}

