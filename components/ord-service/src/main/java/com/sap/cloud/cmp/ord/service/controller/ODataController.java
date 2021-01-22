package com.sap.cloud.cmp.ord.service.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.olingo.jpa.processor.core.api.JPAClaimsPair;
import com.sap.olingo.jpa.processor.core.api.JPAODataCRUDContextAccess;
import com.sap.olingo.jpa.processor.core.api.JPAODataClaimsProvider;
import com.sap.olingo.jpa.processor.core.api.JPAODataGetHandler;

import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.server.api.debug.DefaultDebugSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/${odata.jpa.request_mapping_path}/**")
public class ODataController extends com.sap.cloud.cmp.ord.service.controller.Controller {

    @Autowired
    private JPAODataCRUDContextAccess serviceContext;

    @RequestMapping(value = "**", method = {RequestMethod.GET})
    public void handleODataRequest(HttpServletRequest request, HttpServletResponse response) throws ODataException, IOException {
        final JPAODataGetHandler handler = new JPAODataGetHandler(serviceContext);
        handler.getJPAODataRequestContext().setDebugSupport(new DefaultDebugSupport()); // Use query parameter odata-debug=json to activate.

        try {
            handler.getJPAODataRequestContext().setClaimsProvider(createClaims(request));
            handler.process(request, response);
        } catch (IllegalArgumentException e) {
            super.handleErrorResponse(response, e);
        }
    }

    private JPAODataClaimsProvider createClaims(final HttpServletRequest request) throws IOException {
        final JPAODataClaimsProvider claims = new JPAODataClaimsProvider();

        String tenantID = super.extractInternalTenantIdFromIDToken(request);

        final JPAClaimsPair<UUID> user = new JPAClaimsPair<>(UUID.fromString(tenantID));
        claims.add("tenant_id", user);

        return claims;
    }
}
