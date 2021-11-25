package com.example.routes;

import com.example.dto.ServiceErrorResponse;
import com.example.exceptions.ResourceDuplicationException;
import com.example.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import javax.inject.Inject;
@Slf4j
public class AbstractRestRouteBuilder extends RouteBuilder {

    @Inject
    ObjectMapper mapper;

    @Override
    public void configure() throws Exception {

        onException(ResourceDuplicationException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    ResourceDuplicationException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,ResourceDuplicationException.class);

                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("Resource Duplicate");
                    errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}",406,exception.getMessage()));
                    log.error("Error fpr ResourceDuplicationException  ===> {}",mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setBody(errorResponse);
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(406));

        onException(ResourceNotFoundException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    ResourceNotFoundException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,ResourceNotFoundException.class);

                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("Resource Not Found");
                    errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}",406,exception.getMessage()));
                    log.error("Error fpr ResourceNotFoundException ===> {}",mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setBody(errorResponse);
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(406));

    }
}
