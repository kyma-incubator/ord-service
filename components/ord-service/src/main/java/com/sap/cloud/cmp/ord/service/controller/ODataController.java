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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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

        String tenantID = super.extractInternalTenantIdFromIDToken(request);
        if (tenantID != null && !tenantID.isEmpty()) {
            final JPAClaimsPair<String> user = new JPAClaimsPair<>(tenantID);
            claims.add("tenant_id", user);
        } else {
            logger.warn("Could not determine tenant claim");
        }
        return claims;
    }
}
