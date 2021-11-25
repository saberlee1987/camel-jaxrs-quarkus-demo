package com.example.jaxrs;

import com.example.dto.ServiceErrorResponse;
import com.example.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class ResourceNotFoundExceptionHandler implements ExceptionMapper<ResourceNotFoundException> {
    @Inject
    ObjectMapper mapper;

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse();
        errorResponse.setCode(2);
        errorResponse.setMessage("Resource NotFound");
        errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}",406,exception.getMessage()));
        try {
            log.error("Error fpr ResourceNotFoundException  ===> {}",mapper.writeValueAsString(errorResponse));
        } catch (JsonProcessingException e) {
            log.error("Error fpr ResourceNotFoundException  ===> {}",errorResponse);
        }

        return Response.status(406).entity(errorResponse).build();
    }
}
