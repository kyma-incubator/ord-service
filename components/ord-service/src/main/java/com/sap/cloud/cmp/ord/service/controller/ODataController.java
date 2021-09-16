package com.sap.cloud.cmp.ord.service.controller;

import com.sap.olingo.jpa.processor.core.api.JPAClaimsPair;
import com.sap.olingo.jpa.processor.core.api.JPAODataCRUDContextAccess;
import com.sap.olingo.jpa.processor.core.api.JPAODataClaimsProvider;
import com.sap.olingo.jpa.processor.core.api.JPAODataGetHandler;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.server.api.debug.DefaultDebugSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


@Controller
@RequestMapping("/${odata.jpa.request_mapping_path}/**")
public class ODataController extends com.sap.cloud.cmp.ord.service.controller.Controller {

    @Autowired
    private JPAODataCRUDContextAccess serviceContext;

    private static final Logger logger = LoggerFactory.getLogger(ODataController.class);

    @RequestMapping(value = "**", method = {RequestMethod.GET})
    public void handleODataRequest(HttpServletRequest request, HttpServletResponse response) throws ODataException, IOException {
        final JPAODataGetHandler handler = new JPAODataGetHandler(serviceContext);
        handler.getJPAODataRequestContext().setDebugSupport(new DefaultDebugSupport()); // Use query parameter odata-debug=json to activate.
        handler.getJPAODataRequestContext().setClaimsProvider(createClaims(request));
        handler.process(request, response);
    }

    private JPAODataClaimsProvider createClaims(final HttpServletRequest request) throws IOException {
        final JPAODataClaimsProvider claims = new JPAODataClaimsProvider();
        Pair<String, String> tenantIDs = super.extractTenantsFromIDToken(request);
        if (tenantIDs == null) {
            logger.warn("Could not determine tenants claim");
            return claims;
        }
        String tenantID = tenantIDs.getFirst();
        String providerTenantID = tenantIDs.getSecond();
        if (tenantID != null && !tenantID.isEmpty()) {
            try {
                final JPAClaimsPair<UUID> tenantJPAPair = new JPAClaimsPair<>(UUID.fromString(tenantID));
                final JPAClaimsPair<UUID> providerTenantJPAPair = new JPAClaimsPair<>(UUID.fromString(providerTenantID));
                claims.add("tenant_id", tenantJPAPair);
                claims.add("provider_tenant_id", providerTenantJPAPair);
            } catch (IllegalArgumentException e) {
                logger.warn("Could not parse tenant uuid");
            }
        } else {
            logger.warn("Could not determine tenant from tenants claim");
        }
        return claims;
    }
}
