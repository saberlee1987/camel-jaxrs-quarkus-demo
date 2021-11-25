package com.example.jaxrs;

import com.example.dto.HelloDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/services/jaxrs")
@Singleton
@Slf4j
public class ExampleResource {

    @Inject
    private ObjectMapper mapper;
    @GET
    @Path(value = "/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(@QueryParam(value = "firstName") String firstName,@QueryParam(value = "lastName") String lastName) {
        HelloDto helloDto = new HelloDto();
        helloDto.setMessage(String.format("Hello %s %s",firstName,lastName));
        try{
            log.info("helloDto Message ====> {}",mapper.writeValueAsString(helloDto));
        }catch (Exception ex){
            log.info("helloDto Message ====> {}",helloDto);
        }


        return Response.ok(helloDto,MediaType.APPLICATION_JSON).build();
    }
}