package com.sap.cloud.cmp.ord.service.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.cloud.cmp.ord.service.repository.SpecRepository;
import com.sap.cloud.cmp.ord.service.storage.model.SpecificationEntity;
import com.sap.cloud.cmp.ord.service.token.Token;
import com.sap.cloud.cmp.ord.service.token.TokenParser;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SpecificationsController {

    private static final String MEDIA_TYPE_YAML_VALUE = "text/yaml";

    private static final String NOT_FOUND_MESSAGE = "Not Found";
    private static final String INVALID_TENANT_ID_ERROR_MESSAGE = "Missing or invalid tenantID";

    private static final String UNAUTHORIZED_MSG = "{\n" +
            "  \"error\": {\n" +
            "    \"code\": 401,\n" +
            "    \"status\": \"Unauthorized\",\n" +
            "    \"request\": \"8ed540a8-b4c6-49e3-a5ca-8dc29ba94318\",\n" +
            "    \"message\": \"The request could not be authorized\"\n" +
            "  }\n" +
            "}";

    private SpecRepository specRepository;
    private TokenParser tokenParser;

    public SpecificationsController(@Autowired final SpecRepository specRepository, @Autowired final TokenParser tokenParser) {
      super();
      this.specRepository = specRepository;
      this.tokenParser = tokenParser;
    }

    @GetMapping(value = "/${static.request_mapping_path}/api/{apiId}/specification/{specId}", produces = {MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    @Parameter(name = "Tenant", required = true, description = "Tenant GUID", allowEmptyValue = false, in = ParameterIn.HEADER, content = @Content(schema = @Schema(type = "uuid")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "<api specification in appropriate standard>"), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid tenantID", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "Missing or invalid tenantID"), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "example", value = UNAUTHORIZED_MSG), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "Not Found"), schema = @Schema(implementation = String.class)))
    })
    public void getApiSpec(HttpServletRequest request, HttpServletResponse response, @Parameter(description = "API ID") @PathVariable final String apiId, @Parameter(description = "API specification ID") @PathVariable final String specId) throws IOException {
        Token token = tokenParser.fromRequest(request);
        String tenantID = token == null ? "" : token.extractTenant();
        if (token == null || tenantID == null || tenantID.isEmpty()) {
            respond(response, HttpServletResponse.SC_BAD_REQUEST, MediaType.TEXT_PLAIN_VALUE, INVALID_TENANT_ID_ERROR_MESSAGE);
            return;
        }

        try {
            SpecificationEntity apiSpec = specRepository.getBySpecIdAndApiDefinitionIdAndTenant(UUID.fromString(specId), UUID.fromString(apiId), UUID.fromString(tenantID));
            if (apiSpec == null) {
                respond(response, HttpServletResponse.SC_NOT_FOUND, MediaType.TEXT_PLAIN_VALUE, NOT_FOUND_MESSAGE);
                return;
            }

            respond(response, HttpServletResponse.SC_OK, apiSpec.getApiSpecFormat(), apiSpec.getSpecData());
        } catch (IllegalArgumentException e) {
            respond(response, HttpServletResponse.SC_BAD_REQUEST, MediaType.TEXT_PLAIN_VALUE, e.getMessage());
        }
    }

    @GetMapping(value = "/${static.request_mapping_path}/event/{eventId}/specification/{specId}", produces = {MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    @Parameter(name = "Tenant", description = "Tenant GUID", required = true, allowEmptyValue = false, in = ParameterIn.HEADER, content = @Content(schema = @Schema(type = "uuid")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "<event specification in appropriate standard>"), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid tenantID", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "Missing or invalid tenantID"), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "example", value = UNAUTHORIZED_MSG), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "Not Found"), schema = @Schema(implementation = String.class)))
    })
    public void getEventSpec(HttpServletRequest request, HttpServletResponse response, @Parameter(description = "Event ID") @PathVariable final String eventId, @Parameter(description = "Event specification ID") @PathVariable final String specId) throws IOException {
        Token token = tokenParser.fromRequest(request);
        String tenantID = token == null ? "" : token.extractTenant();
        if (token == null || tenantID == null || tenantID.isEmpty()) {
            respond(response, HttpServletResponse.SC_BAD_REQUEST, MediaType.TEXT_PLAIN_VALUE, INVALID_TENANT_ID_ERROR_MESSAGE);
            return;
        }

        try {
            SpecificationEntity eventSpec = specRepository.getBySpecIdAndEventDefinitionIdAndTenant(UUID.fromString(specId), UUID.fromString(eventId), UUID.fromString(tenantID));
            if (eventSpec == null) {
                respond(response, HttpServletResponse.SC_NOT_FOUND, MediaType.TEXT_PLAIN_VALUE, NOT_FOUND_MESSAGE);
                return;
            }

            respond(response, HttpServletResponse.SC_OK, eventSpec.getEventSpecFormat(), eventSpec.getSpecData());
        } catch (IllegalArgumentException e) {
            respond(response, HttpServletResponse.SC_BAD_REQUEST, MediaType.TEXT_PLAIN_VALUE, e.getMessage());
        }
    }

    @GetMapping(value = "/${static.request_mapping_path}/capability/{capabilityId}/specification/{specId}", produces = {MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    @Parameter(name = "Tenant", description = "Tenant GUID", required = true, allowEmptyValue = false, in = ParameterIn.HEADER, content = @Content(schema = @Schema(type = "uuid")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "<capability specification in appropriate standard>"), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid tenantID", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "Missing or invalid tenantID"), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(name = "example", value = UNAUTHORIZED_MSG), schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, examples = @ExampleObject(name = "example", value = "Not Found"), schema = @Schema(implementation = String.class)))
    })
    public void getCapabilitySpec(HttpServletRequest request, HttpServletResponse response, @Parameter(description = "Capability ID") @PathVariable final String capabilityId, @Parameter(description = "Capability specification ID") @PathVariable final String specId) throws IOException {
        Token token = tokenParser.fromRequest(request);
        String tenantID = token == null ? "" : token.extractTenant();

        if (token == null || tenantID == null || tenantID.isEmpty()) {
            respond(response, HttpServletResponse.SC_BAD_REQUEST, MediaType.TEXT_PLAIN_VALUE, INVALID_TENANT_ID_ERROR_MESSAGE);
            return;
        }

        try {
            SpecificationEntity capabilitySpec = specRepository.getBySpecIdAndCapabilityDefinitionIdAndTenant(UUID.fromString(specId), UUID.fromString(capabilityId), UUID.fromString(tenantID));
            if (capabilitySpec == null) {
                respond(response, HttpServletResponse.SC_NOT_FOUND, MediaType.TEXT_PLAIN_VALUE, NOT_FOUND_MESSAGE);
                return;
            }

            respond(response, HttpServletResponse.SC_OK, capabilitySpec.getCapabilitySpecFormat(), capabilitySpec.getSpecData());
        } catch (IllegalArgumentException e) {
            respond(response, HttpServletResponse.SC_BAD_REQUEST, MediaType.TEXT_PLAIN_VALUE, e.getMessage());
        }
    }

    private void respond(HttpServletResponse response, int statusCode, String contentType, String body) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(contentType);

        response.getWriter().print(body);
    }
}
