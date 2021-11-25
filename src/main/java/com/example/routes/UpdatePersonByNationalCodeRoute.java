package com.example.routes;

import com.example.dto.PersonDto;
import com.example.dto.PersonEntity;
import com.example.dto.ServiceErrorResponse;
import com.example.exceptions.ValidatorException;
import com.example.services.InputValidator;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

@Singleton
public class UpdatePersonByNationalCodeRoute extends AbstractRestRouteBuilder {

    @Inject
    InputValidator validator;

    @Override
    public void configure() throws Exception {
        super.configure();
        rest("/persons")
                .put("/updateByNationalCode/{nationalCode}")
                .id(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE)
                .description("Update Person By nationalCode")
                .consumes(MediaType.APPLICATION_JSON)
                .produces(MediaType.APPLICATION_JSON)
                .responseMessage().code(200).responseModel(PersonEntity.class).example("example1","{\"firstName\" :\"saber\",\"lastName\" : \"Azizi\",\"age\" : 34,\"nationalCode\":\"0079028748\",\"email\" : \"saberazizi66@yahoo.com\",\"mobile\" : \"09365627895\"}").endResponseMessage()
                .responseMessage().code(406).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .enableCORS(true)
                .param().name("nationalCode").type(RestParamType.path).required(true).dataType("string").example("0079028748").endParam()
                .type(PersonDto.class)
                .bindingMode(RestBindingMode.json)
                .route()
                .routeId(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_ROUTE)
                .routeGroup(Routes.UPDATE_PERSON_BY_NATIONAL_CODE_GROUP)
                .log("Request for Update Person By nationalCode nationalCode ${header.nationalCode} ====> ${body}")
                .to("bean-validator://update-person-validator")
                .process(exchange -> {
                    String nationalCode = exchange.getIn().getHeader("nationalCode", String.class);
                    if (!validator.validateNationalCode(nationalCode)){
                        throw new ValidatorException("nationalCode","Please enter valid nationalCode");
                    }
                })
                .to("bean:personService?method=updatePerson")
                .marshal().json(JsonLibrary.Jackson)
                .convertBodyTo(String.class)
                .log("Response for Update Person By nationalCode nationalCode ${header.nationalCode}  ===> ${body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200));

    }
}
