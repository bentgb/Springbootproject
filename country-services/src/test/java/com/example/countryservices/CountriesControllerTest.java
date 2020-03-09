package com.example.countryservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(CountriesController.class)
@Import({CountriesModelAssembler.class})
class CountriesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CountryRepository repository;

    @BeforeEach
    void setUp() {
        when(repository.findAll()).thenReturn(List.of(new Country(1L, "Sweden", "Western Europe", 9016596, 26800),
                new Country(2L, "Denmark", "Western Europe", 5450661, 31100)));
//                ,
//                new Country(3L, "Estonia", "Baltics", 1324333, 12300),
//                new Country(4L, "Czech Republic", "Eastern Europe", 10235455, 15700),
//                new Country(5L, "Fiji", "Oceania", 905949, 5800),
//                new Country(6L, "Mali", "SubSaharan Africa", 11716829, 900)))

                when(repository.findById(1L)).thenReturn(Optional.of(new Country(1L, "Sweden", "Western Europe", 9016596, 26800)));
                when(repository.existsById(7L)).thenReturn(false);
                when(repository.existsById(3L)).thenReturn(true);
                when(repository.save(any(Country.class))).thenAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            var c = (Country) args[0];
            return new Country(1L, c.getName(), c.getRegion(), c.getPopulation(), c.getGdpPerCapita());
        });

    }


    @Test
    void getAllReturnsListOfAllCountries() throws Exception {
        mockMvc.perform(
                get("http://localhost:8080/api/countries").contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.countryList[0]._links.self.href", is("http://localhost:8080/api/countries/1")))
                .andExpect(jsonPath("_embedded.countryList[0].name", is("Sweden")))
                .andExpect(jsonPath("_embedded.countryList[0].region", is("Western Europe")))
                .andExpect(jsonPath("_embedded.countryList[0].population", is(9016596)))
                .andExpect(jsonPath("_embedded.countryList[0].gdpPerCapita", is(26800)))
                .andExpect(jsonPath("_embedded.countryList[1]._links.self.href", is("http://localhost:8080/api/countries/2")))
                .andExpect(jsonPath("_embedded.countryList[1].name", is("Denmark")))
                .andExpect(jsonPath("_embedded.countryList[1].region", is("Western Europe")))
                .andExpect(jsonPath("_embedded.countryList[1].population", is(5450661)))
                .andExpect(jsonPath("_embedded.countryList[1].gdpPerCapita", is(31100)));


        //Build json paths with: https://jsonpath.com/
    }

    @Test
    @DisplayName("Calls Get method with url /api/countries/1")
    void getOnePersonWithValidIdOne() throws Exception {
        mockMvc.perform(
                get("http://localhost:8080/api/countries/1").accept("application/hal+json"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("content[0].links[2].rel", is("self")))
                .andExpect(jsonPath("_links.self.href", is("http://localhost:8080/api/countries/1")));
    }


    @Test
    @DisplayName("Calls RemoteCall method ")
    void getRemoteCall() throws Exception {
        mockMvc.perform(
                get("http://localhost:8080/api/countries/remotecall").accept("text/plain;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Calls Get method with invalid id url /api/countries/7")
    void getOnePersonWithInValidIdThree() throws Exception {
        mockMvc.perform(
                get("/api/countries/7").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Delete country")
    void deleteGuestReturnsOk() throws Exception {
        mockMvc.perform(
                delete("http://localhost:8080/api/countries/3"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Modify only name")
    public void patchName() throws Exception {
        mockMvc.perform(patch("http://localhost:8080/api/countries/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Sverige\"}"))
                .andExpect(jsonPath("name", is("Sverige")))
                .andExpect(jsonPath("region", is("Western Europe")))
                .andExpect(jsonPath("population", is (9016596)))
                .andExpect(jsonPath("gdpPerCapita", is(26800)))
                .andExpect(status().isOk());
    }

    @Test
    void addNewCountryWithPostReturnsCreatedCountry() throws Exception {
        mockMvc.perform(
                post("http://localhost:8080/api/countries/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Sweden\",\"region\":\"Western Europe\",\"population\":9016596, \"gdpPerCapita\": 26800}"))
                .andExpect(status().isCreated());
    }

}