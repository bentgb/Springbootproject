package com.example.countryservices;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {


    Country findByName(String name);

}
