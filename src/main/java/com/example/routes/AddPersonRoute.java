package com.example.routes;

import com.example.dto.PersonDto;
import com.example.dto.PersonEntity;
import com.example.dto.ServiceErrorResponse;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

@Singleton
public class AddPersonRoute extends AbstractRestRouteBuilder {
    @Override
    public void configure() throws Exception {
        super.configure();
        rest("/persons")
                .post("/add")
                .id(Routes.ADD_PERSON_ROUTE)
                .description("Add Person")
                .consumes(MediaType.APPLICATION_JSON)
                .produces(MediaType.APPLICATION_JSON)
                .responseMessage().code(200).responseModel(PersonEntity.class).example("example1","{\"firstName\" :\"saber\",\"lastName\" : \"Azizi\",\"age\" : 34,\"nationalCode\":\"0079028748\",\"email\" : \"saberazizi66@yahoo.com\",\"mobile\" : \"09365627895\"}").endResponseMessage()
                .responseMessage().code(406).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .enableCORS(true)
                .type(PersonDto.class)
                .bindingMode(RestBindingMode.json)
                .route()
                .routeId(Routes.ADD_PERSON_ROUTE)
                .routeGroup(Routes.ADD_PERSON_GROUP)
                .log("Request for addPerson ====> ${body}")
                .to("bean:personService?method=addPerson")
                .marshal().json(JsonLibrary.Jackson)
                .convertBodyTo(String.class)
                .log("Response for add Person ===> ${body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200));

    }
}
