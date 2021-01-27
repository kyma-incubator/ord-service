package com.sap.cloud.cmp.ord.service.controller;


import com.sap.cloud.cmp.ord.service.repository.ApiSpecRepository;
import com.sap.cloud.cmp.ord.service.repository.EventSpecRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class SpecificationsController {

    private static final String UNAUTHORIZED_MSG = "{\n" +
            "  \"error\": {\n" +
            "    \"code\": 401,\n" +
            "    \"status\": \"Unauthorized\",\n" +
            "    \"request\": \"8ed540a8-b4c6-49e3-a5ca-8dc29ba94318\",\n" +
            "    \"message\": \"The request could not be authorized\"\n" +
            "  }\n" +
            "}";

    @Autowired
    private ApiSpecRepository apiSpecRepository;

    @Autowired
    private EventSpecRepository eventSpecRepository;


    @RequestMapping(value = "/${static.request_mapping_path}/api/{id}/specification", method = { RequestMethod.GET }, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @ApiImplicitParam(name = "Tenant", value = "Tenant GUID", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = UUID.class)
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="OK", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example",value = "<api specification in appropriate standard>"), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description="Missing or invalid tenantID", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example",value = "Missing or invalid tenantID"),schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description="Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "example",value = UNAUTHORIZED_MSG),schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description="Not Found", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example",value = "Not Found"),schema = @Schema(implementation = String.class)))
    })
    public String getApiSpec(@PathVariable final String id) {
        return apiSpecRepository.getOne(UUID.fromString(id)).getSpecData();
    }

    @RequestMapping(value = "/${static.request_mapping_path}/event/{id}/specification", method = { RequestMethod.GET }, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @ApiImplicitParam(name = "Tenant", value = "Tenant GUID", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = UUID.class)
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="OK", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example",value = "<event specification in appropriate standard>"), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description="Missing or invalid tenantID", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example",value = "Missing or invalid tenantID"),schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description="Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "example",value = UNAUTHORIZED_MSG),schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description="Not Found", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example",value = "Not Found"),schema = @Schema(implementation = String.class)))
    })
    public String getEventSpec(@PathVariable final String id) {
        return eventSpecRepository.getOne(UUID.fromString(id)).getSpecData();
    }

}
