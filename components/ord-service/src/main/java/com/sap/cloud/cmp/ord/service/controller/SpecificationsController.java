package com.sap.cloud.cmp.ord.service.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.cloud.cmp.ord.service.repository.ApiSpecRepository;
import com.sap.cloud.cmp.ord.service.repository.EventSpecRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.sap.cloud.cmp.ord.service.storage.model.APISpecificationEntity;
import com.sap.cloud.cmp.ord.service.storage.model.EventSpecificationEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpecificationsController extends com.sap.cloud.cmp.ord.service.controller.Controller {

    private final String NOT_FOUND_MESSAGE = "Not Found";
    private final String INVALID_TENANT_ID_ERROR_MESSAGE = "Missing or invalid tenantID";

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
    public String getApiSpec(HttpServletRequest request, HttpServletResponse response, @PathVariable final String id) throws IOException {
        APISpecificationEntity apiSpec = new APISpecificationEntity();
        String tenantID = extractInternalTenantIdFromIDToken(request);

        try {
            apiSpec = apiSpecRepository.getByApiDefinitionIdAndTenant(UUID.fromString(id), UUID.fromString(tenantID));
            if (apiSpec == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return NOT_FOUND_MESSAGE;
            }
        } catch (IllegalArgumentException e) {
            handleErrorResponse(response, INVALID_TENANT_ID_ERROR_MESSAGE);
        }
        return apiSpec.getSpecData();
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
    public String getEventSpec(HttpServletRequest request, HttpServletResponse response, @PathVariable final String id) throws IOException {
        EventSpecificationEntity eventSpec = new EventSpecificationEntity();
        String tenantID = super.extractInternalTenantIdFromIDToken(request);

        try {
            eventSpec = eventSpecRepository.getByEventDefinitionIdAndTenant(UUID.fromString(id), UUID.fromString(tenantID));
            if (eventSpec == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return NOT_FOUND_MESSAGE;
            }
        } catch (IllegalArgumentException e) {
            handleErrorResponse(response, INVALID_TENANT_ID_ERROR_MESSAGE);
        }
        return eventSpec.getSpecData();
    }


    private void handleErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);

        response.getWriter().print(errorMessage);
    }
}

