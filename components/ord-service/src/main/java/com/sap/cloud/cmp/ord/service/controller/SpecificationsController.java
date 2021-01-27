package com.sap.cloud.cmp.ord.service.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.cloud.cmp.ord.service.repository.ApiSpecRepository;
import com.sap.cloud.cmp.ord.service.repository.EventSpecRepository;
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
    private final String INVALID_TENANT_ID_ERROR_MESSAGE = "Missing or invalid tenantID.";

    @Autowired
    private ApiSpecRepository apiSpecRepository;

    @Autowired
    private EventSpecRepository eventSpecRepository;


    @RequestMapping(value = "/${odata.jpa.request_mapping_path}/api/{id}/specification", method = {RequestMethod.GET}, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
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
            super.handleErrorResponse(response, INVALID_TENANT_ID_ERROR_MESSAGE, MediaType.TEXT_PLAIN_VALUE);
        }
        return apiSpec.getSpecData();
    }

    @RequestMapping(value = "/${odata.jpa.request_mapping_path}/event/{id}/specification", method = {RequestMethod.GET}, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
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
            super.handleErrorResponse(response, INVALID_TENANT_ID_ERROR_MESSAGE, MediaType.TEXT_PLAIN_VALUE);
        }
        return eventSpec.getSpecData();
    }
}

