package com.example.jaxrs;

import com.example.dto.PersonDto;
import com.example.dto.PersonEntity;
import com.example.services.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(value = "/services/jaxrs/persons")
@Singleton
@Slf4j
public class PersonResource {

    @Inject
    PersonService personService;

    @Inject
    ObjectMapper mapper;

    @POST
    @Path(value = "/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(PersonDto personDto){
        try {
            log.info("Request for add Person with body ===> {}",mapper.writeValueAsString(personDto));
        } catch (JsonProcessingException e) {
            log.info("Request for add Person with body ===> {}",personDto);
        }

        PersonEntity personEntity = this.personService.addPerson(personDto);
        try {
            log.info("Response for add Person with body ===> {}",mapper.writeValueAsString(personEntity));
        } catch (JsonProcessingException e) {
            log.info("Response for add Person with body ===> {}",personEntity);
        }
        return Response.ok(personEntity).build();
    }


    @GET
    @Path(value = "/findByNationalCode/{nationalCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findByNationalCode( @PathParam(value = "nationalCode") String nationalCode){
        log.info("Request for add Person with nationalCode ===> {}",nationalCode);

        PersonEntity personEntity = this.personService.findByNationalCode(nationalCode);
        try {
            log.info("Response for add Person with nationalCode ===> {} ===> {}",nationalCode,mapper.writeValueAsString(personEntity));
        } catch (JsonProcessingException e) {
            log.info("Response for add Person with nationalCode ===> {} ===> {}",nationalCode,personEntity);
        }
        return Response.ok(personEntity).build();
    }
}
