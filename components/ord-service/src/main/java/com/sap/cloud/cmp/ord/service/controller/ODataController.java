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
import java.util.UUID;


@Controller
@RequestMapping("/${odata.jpa.request_mapping_path}/**")
public class ODataController extends com.sap.cloud.cmp.ord.service.controller.Controller {

    @Autowired
    private JPAODataCRUDContextAccess serviceContext;

    private static final Logger logger = LoggerFactory.getLogger(ODataController.class);
    private final String PUBLIC_VISIBILITY = "public";
    private final String INTERNAL_VISIBILITY = "internal";
    private final String PRIVATE_VISIBILITY = "private";

    @RequestMapping(value = "**", method = {RequestMethod.GET})
    public void handleODataRequest(HttpServletRequest request, HttpServletResponse response) throws ODataException, IOException {
        final JPAODataGetHandler handler = new JPAODataGetHandler(serviceContext);
        handler.getJPAODataRequestContext().setDebugSupport(new DefaultDebugSupport()); // Use query parameter odata-debug=json to activate.
        handler.getJPAODataRequestContext().setClaimsProvider(createClaims(request));
        handler.process(request, response);
    }

    private JPAODataClaimsProvider createClaims(final HttpServletRequest request) throws IOException {
        final JPAODataClaimsProvider claims = new JPAODataClaimsProvider();

        String tenantID = super.extractTenantFromIDToken(request);
        if (tenantID == null || tenantID.isEmpty()) {
            logger.warn("Could not determine tenant claim");
            return claims;
        }

        final JPAClaimsPair<UUID> tenantIDJPAPair = new JPAClaimsPair<>(UUID.fromString(tenantID));
        claims.add("tenant_id", tenantIDJPAPair);

        final JPAClaimsPair<String> publicVisibilityScopeJPAPair = new JPAClaimsPair<>(PUBLIC_VISIBILITY);
        claims.add("visibility_scope", publicVisibilityScopeJPAPair);

        if (super.isInternalVisibilityScopePresent(request)) {
            final JPAClaimsPair<String> internalVisibilityScopeJPAPair = new JPAClaimsPair<>(INTERNAL_VISIBILITY);
            claims.add("visibility_scope", internalVisibilityScopeJPAPair);

            final JPAClaimsPair<String> privateVisibilityScopeJPAPair = new JPAClaimsPair<>(PRIVATE_VISIBILITY);
            claims.add("visibility_scope", privateVisibilityScopeJPAPair);
        }

        return claims;
    }
}
