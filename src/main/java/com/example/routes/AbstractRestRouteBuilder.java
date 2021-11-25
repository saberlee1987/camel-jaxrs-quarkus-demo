package com.example.routes;

import com.example.dto.ServiceErrorResponse;
import com.example.dto.ValidationDto;
import com.example.exceptions.ResourceDuplicationException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.exceptions.ValidatorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AbstractRestRouteBuilder extends RouteBuilder {

    @Inject
    ObjectMapper mapper;

    @Override
    public void configure() throws Exception {


        onException(ValidatorException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    ValidatorException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, ValidatorException.class);
                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(4);
                    errorResponse.setMessage("Validation Error");
                    List<ValidationDto> validations = new ArrayList<>();
                    ValidationDto validationDto = new ValidationDto();
                    validationDto.setFieldName(exception.getFieldName());
                    validationDto.setDetailMessage(exception.getDetailMessage());
                    validations.add(validationDto);

                    errorResponse.setValidations(validations);
                    log.error("Error fpr BeanValidationException  ===> {}", mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setBody(errorResponse);
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406));

        onException(BeanValidationException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    BeanValidationException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, BeanValidationException.class);
                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(4);
                    errorResponse.setMessage("Validation Error");
                    List<ValidationDto> validations = new ArrayList<>();
                    for (ConstraintViolation<Object> v : exception.getConstraintViolations()) {
                        ValidationDto validationDto = new ValidationDto();
                        validationDto.setFieldName(v.getPropertyPath().toString());
                        validationDto.setDetailMessage(v.getMessage());
                        validations.add(validationDto);
                    }
                    errorResponse.setValidations(validations);
                    log.error("Error fpr BeanValidationException  ===> {}", mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setBody(errorResponse);
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406));

        onException(ResourceDuplicationException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    ResourceDuplicationException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, ResourceDuplicationException.class);

                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("Resource Duplicate");
                    errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}", 406, exception.getMessage()));
                    log.error("Error fpr ResourceDuplicationException  ===> {}", mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setBody(errorResponse);
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406));

        onException(ResourceNotFoundException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    ResourceNotFoundException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, ResourceNotFoundException.class);

                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("Resource Not Found");
                    errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}", 406, exception.getMessage()));
                    log.error("Error fpr ResourceNotFoundException ===> {}", mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setBody(errorResponse);
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(406));

    }
}
