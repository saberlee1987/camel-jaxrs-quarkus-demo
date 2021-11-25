package com.example.routes;

import com.example.dto.ServiceErrorResponse;
import com.example.dto.ServiceResponse;
import com.example.exceptions.ResourceNotFoundException;
import com.example.exceptions.ValidatorException;
import com.example.services.InputValidator;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

@Singleton
public class DeletePersonByNationalCodeRoute extends AbstractRestRouteBuilder{

    @Inject
    InputValidator validator;

    @Override
    public void configure() throws Exception {
        super.configure();

        rest("/persons")
                .delete("/deleteByNationalCode/{nationalCode}")
                .id(Routes.DELETE_BY_NATIONAL_CODE_ROUTE)
                .description("Delete Person By NationalCode")
                .produces(MediaType.APPLICATION_JSON)
                .responseMessage().code(200).responseModel(ServiceResponse.class).endResponseMessage()
                .responseMessage().code(406).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .param().name("nationalCode").type(RestParamType.path).required(true).dataType("string").example("0079028748").endParam()
                .enableCORS(true)
                .route()
                .routeId(Routes.DELETE_BY_NATIONAL_CODE_ROUTE)
                .routeGroup(Routes.DELETE_BY_NATIONAL_CODE_GROUP)
                .log("Request Delete Person By nationalCode ====> ${header.nationalCode}")
                .process(exchange -> {
                    String nationalCode = exchange.getIn().getHeader("nationalCode", String.class);
                    if (!validator.validateNationalCode(nationalCode)){
                        throw new ValidatorException("nationalCode","Please enter valid nationalCode");
                    }
                })
                .to("bean:personService?method=deletePersonByNationalCode")
                .convertBodyTo(String.class)
                .marshal().json(JsonLibrary.Jackson)
                .log("Response find Person By nationalCode ====> ${header.nationalCode} ===> ${body}")
                .process(exchange -> {
                    boolean isDeleted = exchange.getIn().getBody(Boolean.class);
                    ServiceResponse response = new ServiceResponse();
                    if (isDeleted){
                        response.setCode(0);
                        response.setMessage("Person deleted successfully");
                    }else{
                        throw new ResourceNotFoundException(String.format("Person does not exist with nationalCode %s", exchange.getIn().getHeader("nationalCode")));
                    }
                    exchange.getIn().setBody(response);
                })
                .unmarshal().json(JsonLibrary.Jackson)
                .log("Response find Person By nationalCode ====> ${header.nationalCode} ===> ${body}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200));
    }
}
