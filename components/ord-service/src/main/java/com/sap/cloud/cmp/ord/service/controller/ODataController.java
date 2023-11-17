package com.sap.cloud.cmp.ord.service.controller;

import com.sap.cloud.cmp.ord.service.common.Common;
import com.sap.cloud.cmp.ord.service.token.Token;
import com.sap.cloud.cmp.ord.service.token.TokenParser;
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
public class ODataController {

    @Autowired
    private TokenParser tokenParser;

    @Autowired
    private JPAODataCRUDContextAccess serviceContext;

    private static final Logger logger = LoggerFactory.getLogger(ODataController.class);
    private final String PUBLIC_VISIBILITY = "public";
    private final String INTERNAL_VISIBILITY = "internal";
    private final String PRIVATE_VISIBILITY = "private";
    private final String EMPTY_FORMATIONS_DEFAULT_FORMATION_ID_CLAIM = "eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee";
    private final String DEFAULT_TENANT_ID = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb";

    @RequestMapping(value = "**", method = {RequestMethod.GET})
    public void handleODataRequest(HttpServletRequest request, HttpServletResponse response) throws ODataException, IOException {
        final JPAODataGetHandler handler = new JPAODataGetHandler(serviceContext);
        handler.getJPAODataRequestContext().setDebugSupport(new DefaultDebugSupport()); // Use query parameter odata-debug=json to activate.

        Token token = tokenParser.fromRequest(request);
        String tenantId = token == null ? "" : token.extractTenant();

        // store the tenant id, so it is available to post-OData processing filters
        request.setAttribute(Common.REQUEST_ATTRIBUTE_TENANT_ID, tenantId);

        handler.getJPAODataRequestContext().setClaimsProvider(createClaims(token, tenantId));
        handler.process(request, response);
    }

    private JPAODataClaimsProvider createClaims(final Token token, String tenantID) throws IOException {
        final JPAODataClaimsProvider claims = new JPAODataClaimsProvider();

        if (token == null) {
            logger.warn("Could not determine claims because token is null");
            return claims;
        }

        if (tenantID == null || tenantID.isEmpty()) {
            logger.warn("Could not determine tenant claim");
            return claims;
        }

        boolean shouldUseDefaultTenant = false;
        if (token.getFormationIDsClaims().isEmpty()) {
            logger.warn("Could not determine formation claim");
            claims.add("formation_scope", new JPAClaimsPair<>(UUID.fromString(EMPTY_FORMATIONS_DEFAULT_FORMATION_ID_CLAIM))); // in the consumer-provider flow, if there are currently no formations the rtCtx is part of; we will return empty array this way instead of misleading claims error
        } else {
            shouldUseDefaultTenant = true; // when we know that the filtering will be based on formation_id/s, we want to set a default tenant_id
            for (String formationID : token.getFormationIDsClaims()) {
                claims.add("formation_scope", new JPAClaimsPair<>(UUID.fromString(formationID)));
            }
        }

        if (shouldUseDefaultTenant) {
            final JPAClaimsPair<UUID> tenantIDJPAPair = new JPAClaimsPair<>(UUID.fromString(DEFAULT_TENANT_ID));
            claims.add("tenant_id", tenantIDJPAPair);
        } else {
            final JPAClaimsPair<UUID> tenantIDJPAPair = new JPAClaimsPair<>(UUID.fromString(tenantID));
            claims.add("tenant_id", tenantIDJPAPair);
        }

        final JPAClaimsPair<String> publicVisibilityScopeJPAPair = new JPAClaimsPair<>(PUBLIC_VISIBILITY);
        claims.add("visibility_scope", publicVisibilityScopeJPAPair);

        if (token.isInternalVisibilityScopePresent()) {
            final JPAClaimsPair<String> internalVisibilityScopeJPAPair = new JPAClaimsPair<>(INTERNAL_VISIBILITY);
            claims.add("visibility_scope", internalVisibilityScopeJPAPair);

            final JPAClaimsPair<String> privateVisibilityScopeJPAPair = new JPAClaimsPair<>(PRIVATE_VISIBILITY);
            claims.add("visibility_scope", privateVisibilityScopeJPAPair);
        }

        return claims;
    }
}
