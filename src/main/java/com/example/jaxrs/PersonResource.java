package com.example.jaxrs;

import com.example.annotations.NationalCode;
import com.example.dto.PersonDto;
import com.example.dto.PersonEntity;
import com.example.dto.ServiceResponse;
import com.example.exceptions.ResourceNotFoundException;
import com.example.services.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    public Response addPerson(@Valid PersonDto personDto) {
        try {
            log.info("Request for add Person with body ===> {}", mapper.writeValueAsString(personDto));
        } catch (JsonProcessingException e) {
            log.info("Request for add Person with body ===> {}", personDto);
        }

        PersonEntity personEntity = this.personService.addPerson(personDto);
        try {
            log.info("Response for add Person with body ===> {}", mapper.writeValueAsString(personEntity));
        } catch (JsonProcessingException e) {
            log.info("Response for add Person with body ===> {}", personEntity);
        }
        return Response.ok(personEntity).build();
    }

    @PUT
    @Path(value = "/updateByNationalCode/{nationalCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePersonByNationalCode(@Valid PersonDto personDto, @PathParam(value = "nationalCode") @NotBlank(message = "nationalCode is Required") @NationalCode(message = "Please Enter valid nationalCode") String nationalCode) {
        try {
            log.info("Request for update Person with nationalCode {}, body ===> {}", nationalCode, mapper.writeValueAsString(personDto));
        } catch (JsonProcessingException e) {
            log.info("Request for update Person with  with nationalCode {} body ===> {}", nationalCode, personDto);
        }

        PersonEntity personEntity = this.personService.updatePerson(personDto, nationalCode);
        try {
            log.info("Response for add Person with nationalCode {}, body ===> {}", nationalCode, mapper.writeValueAsString(personEntity));
        } catch (JsonProcessingException e) {
            log.info("Response for add Person with nationalCode {}, body ===> {}", nationalCode, personEntity);
        }
        return Response.ok(personEntity).build();
    }


    @GET
    @Path(value = "/findByNationalCode/{nationalCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findByNationalCode(@PathParam(value = "nationalCode") @NotBlank(message = "nationalCode is Required") @NationalCode(message = "Please Enter valid nationalCode") String nationalCode) {
        log.info("Request for add Person with nationalCode ===> {}", nationalCode);

        PersonEntity personEntity = this.personService.findByNationalCode(nationalCode);
        try {
            log.info("Response for add Person with nationalCode ===> {} ===> {}", nationalCode, mapper.writeValueAsString(personEntity));
        } catch (JsonProcessingException e) {
            log.info("Response for add Person with nationalCode ===> {} ===> {}", nationalCode, personEntity);
        }
        return Response.ok(personEntity).build();
    }

    @DELETE
    @Path(value = "/deleteByNationalCode/{nationalCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteByNationalCode(@PathParam(value = "nationalCode") @NotBlank(message = "nationalCode is Required") @NationalCode(message = "Please Enter valid nationalCode") String nationalCode) {
        log.info("Request for delete Person with nationalCode ===> {}", nationalCode);

        boolean deletePerson = this.personService.deletePersonByNationalCode(nationalCode);
        ServiceResponse response = new ServiceResponse();
        if (deletePerson) {
            response.setCode(0);
            response.setMessage("Person deleted successfully");
        } else {
            throw new ResourceNotFoundException(String.format("Person does not exist with nationalCode %s", nationalCode));
        }
        try {
            log.info("Response for delete Person with nationalCode ===> {} ===> {}", nationalCode, mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            log.info("Response for delete Person with nationalCode ===> {} ===> {}", nationalCode, response);
        }
        return Response.ok(response).build();
    }
}
