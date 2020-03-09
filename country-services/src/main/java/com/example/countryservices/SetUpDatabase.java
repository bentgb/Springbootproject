package com.example.countryservices;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class SetUpDatabase {

    @Bean
    CommandLineRunner initDatabase(CountryRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                //New empty database, adding some countries
                log.info("Added to database " + repository.save(new Country(0L, "Sweden", "Western Europe", 9016596, 26800)));
                log.info("Added to database " + repository.save(new Country(0L, "Denmark", "Western Europe", 5450661, 31100)));
                log.info("Added to database " + repository.save(new Country(0L, "Estonia", "Baltics", 1324333, 12300)));
                log.info("Added to database " + repository.save(new Country(0L, "Czech Republic", "Eastern Europe", 10235455, 15700)));
                log.info("Added to database " + repository.save(new Country(0L, "Fiji", "Oceania", 905949, 5800)));
                log.info("Added to database " + repository.save(new Country(0L, "Mali", "SubSaharan Africa", 11716829, 900)));
            }
        };
    }

    @Bean
    @LoadBalanced
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


}
