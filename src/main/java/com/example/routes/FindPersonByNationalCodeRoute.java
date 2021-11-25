package com.example.routes;

import com.example.dto.PersonEntity;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

@Singleton
public class FindPersonByNationalCodeRoute  extends AbstractRestRouteBuilder{

    @Override
    public void configure() throws Exception {
        super.configure();

        rest("/persons")
                .get("/findByNationalCode/{nationalCode}")
                .id(Routes.FIND_BY_NATIONAL_CODE_ROUTE)
                .description("find Person By NationalCode")
                .produces(MediaType.APPLICATION_JSON)
                .responseMessage().code(200).responseModel(PersonEntity.class).endResponseMessage()
                .responseMessage().code(406).responseModel(PersonEntity.class).endResponseMessage()
                .param().name("nationalCode").type(RestParamType.path).required(true).dataType("string").example("0079028748").endParam()
                .enableCORS(true)
                .route()
                .routeId(Routes.FIND_BY_NATIONAL_CODE_ROUTE)
                .routeGroup(Routes.FIND_BY_NATIONAL_CODE_GROUP)
                .log("Request find Person By nationalCode ====> ${header.nationalCode}")
                .to("bean:personService?method=findByNationalCode")
                .marshal().json(JsonLibrary.Jackson)
                .convertBodyTo(String.class)
                .log("Response find Person By nationalCode ====> ${header.nationalCode} ===> ${body}")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200));
    }
}
