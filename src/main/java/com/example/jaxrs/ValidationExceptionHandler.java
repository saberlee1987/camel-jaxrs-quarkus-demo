package com.example.jaxrs;

import com.example.dto.ServiceErrorResponse;
import com.example.dto.ValidationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
@Slf4j
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    @Inject
    ObjectMapper mapper;

    @Override
    public Response toResponse(ConstraintViolationException  exception) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse();
        errorResponse.setCode(4);
        errorResponse.setMessage("Validation Error");
        List<ValidationDto> validations = new ArrayList<>();
        for (ConstraintViolation<?> v : exception.getConstraintViolations()) {
            ValidationDto validationDto = new ValidationDto();
            validationDto.setFieldName(v.getPropertyPath().toString());
            validationDto.setDetailMessage(v.getMessage());
            validations.add(validationDto);
        }
        errorResponse.setValidations(validations);
        try {
            log.error("Error fpr BeanValidationException  ===> {}", mapper.writeValueAsString(errorResponse));
        } catch (JsonProcessingException e) {
            log.error("Error fpr BeanValidationException  ===> {}", errorResponse);
        }
        return Response.status(406).entity(errorResponse).build();
    }
}
