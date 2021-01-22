package com.sap.cloud.cmp.ord.service.controller;


import java.io.IOException;
import java.util.List;
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
public class SpecificationsController {

    @Autowired
    private ApiSpecRepository apiSpecRepository;

    @Autowired
    private EventSpecRepository eventSpecRepository;


    @RequestMapping(value = "/${odata.jpa.request_mapping_path}/api/{id}/specification", method = { RequestMethod.GET }, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getApiSpec(HttpServletRequest request, HttpServletResponse response, @PathVariable final String id) {
        String tenantHeader = request.getHeader("Tenant");
        APISpecificationEntity apiSpec = apiSpecRepository.getByApiDefinitionIdAndTenant(UUID.fromString(id), UUID.fromString(tenantHeader));
        if (apiSpec == null) {
            response.setStatus(404);
            return "Not Found";
        }
        return apiSpec.getSpecData();
    }

    @RequestMapping(value = "/${odata.jpa.request_mapping_path}/event/{id}/specification", method = { RequestMethod.GET }, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getEventSpec(HttpServletRequest request, HttpServletResponse response, @PathVariable final String id) throws IOException {
        String tenantHeader = request.getHeader("Tenant");
        EventSpecificationEntity eventSpec = eventSpecRepository.getByEventDefinitionIdAndTenant(UUID.fromString(id), UUID.fromString(tenantHeader));
        if (eventSpec == null) {
            response.setStatus(404);
            return "Not Found";
        }
        return eventSpec.getSpecData();
    }
}
