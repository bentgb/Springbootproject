package com.example.countryservices;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/countries")
@Slf4j
public class CountriesController {

//    @Autowired
//    RestTemplate restTemplate;

      HttpClient client;

    final CountryRepository repository;
    private final CountriesModelAssembler assembler;


    public CountriesController(CountryRepository storage, CountriesModelAssembler countriesModelAssembler) {
        this.repository = storage;
        this.assembler = countriesModelAssembler;
    }
//
//    @GetMapping(value = "/randomCountry")
//    public Country callCountries(){
//        return restTemplate.getForObject("http://Countries-Service/Country",Country.class);
//    }



    @GetMapping
    public CollectionModel<EntityModel<Country>> all() {
        log.debug("All countries called");
        return assembler.toCollectionModel(repository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Country>> one(@PathVariable long id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        log.info("POST create Country " + country);
        var c = repository.save(country);
        log.info("Saved to repository " + c);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(CountriesController.class).slash(c.getId()).toUri());
        //headers.add("Location", "/api/persons/" + p.getId());
        return new ResponseEntity<>(c, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCountry(@PathVariable Long id) {
        if (repository.existsById(id)) {
            //log.info("Product deleted");
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    ResponseEntity<Country> replaceCountry(@RequestBody Country newCountry, @PathVariable Long id) {
        return repository.findById(id)
                .map(country -> {
                    country.setName(newCountry.getName());
                    repository.save(country);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(linkTo(CountriesController.class).slash(country.getId()).toUri());
                    return new ResponseEntity<>(country, headers, HttpStatus.OK);
                })
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    ResponseEntity<Country> modifyCountry(@RequestBody Country newCountry, @PathVariable Long id) {
        return repository.findById(id)
                .map(country -> {
                    if (newCountry.getName() != null)
                        country.setName(newCountry.getName());

                    repository.save(country);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(linkTo(CountriesController.class).slash(country.getId()).toUri());
                    return new ResponseEntity<>(country, headers, HttpStatus.OK);
                })
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping (value = "/remotecall")
    public String remote() throws IOException, InterruptedException {
        //Java 11 HttpClient, works both as synchron and asynchron client
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://restcountries.eu/rest/v2/all"))
                .build();

        var json = client.send(request, HttpResponse.BodyHandlers.ofString());
        return json.body();

        //Spring blocking RestTemplate
        // var greeting = restTemplate.getForEntity("https://ron-swanson-quotes.herokuapp.com/v2/quotes",String[].class);
        // return greeting.getBody()[0];
    }
}
