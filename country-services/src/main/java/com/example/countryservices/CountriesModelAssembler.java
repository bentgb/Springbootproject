package com.example.countryservices;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class CountriesModelAssembler implements RepresentationModelAssembler<Country, EntityModel<Country>> {

    //http://stateless.co/hal_specification.html

    @Override
    public EntityModel<Country> toModel(Country country) {
        return new EntityModel<>(country,
                linkTo(methodOn(CountriesController.class).one(country.getId())).withSelfRel(),
                linkTo(methodOn(CountriesController.class).all()).withRel("countries"));
    }

    @Override
    public CollectionModel<EntityModel<Country>> toCollectionModel(Iterable<? extends Country> entities) {
        var collection = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(collection,
                linkTo(methodOn(CountriesController.class).all()).withSelfRel());
    }
}