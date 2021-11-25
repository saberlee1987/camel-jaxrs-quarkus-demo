package com.example.routes;

import com.example.dto.HelloDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HelloRoute extends RouteBuilder {

    @Inject
    private ObjectMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(HelloRoute.class);
    @Override
    public void configure() throws Exception {

        from("platform-http:/hello")
                .log("header firstName is ${header.firstName}")
                .log("header lastName is ${header.lastName}")
                .process(exchange -> {
                    String firstName = exchange.getIn().getHeader("firstName",String.class);
                    String lastName = exchange.getIn().getHeader("lastName",String.class);
                    HelloDto helloDto = new HelloDto();
                    helloDto.setMessage(String.format("Hello %s %s",firstName,lastName));

                    log.info("helloDto Message ====> {}",mapper.writeValueAsString(helloDto));

                    exchange.getIn().setBody(helloDto);
                })
                .marshal().json(JsonLibrary.Jackson,HelloDto.class,true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200));
    }
}
