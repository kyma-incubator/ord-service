package com.sap.cloud.cmp.ord.service.controller;

import java.io.IOException;
import java.util.UUID;

import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.server.api.debug.DefaultDebugSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sap.cloud.cmp.ord.service.common.Common;
import com.sap.cloud.cmp.ord.service.token.Token;
import com.sap.cloud.cmp.ord.service.token.TokenParser;
import com.sap.olingo.jpa.processor.core.api.JPAClaimsPair;
import com.sap.olingo.jpa.processor.core.api.JPAODataClaimsProvider;
import com.sap.olingo.jpa.processor.core.api.JPAODataRequestContext;
import com.sap.olingo.jpa.processor.core.api.JPAODataRequestHandler;
import com.sap.olingo.jpa.processor.core.api.JPAODataSessionContextAccess;
import com.sap.olingo.jpa.processor.core.api.example.JPAExampleCUDRequestHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/${odata.jpa.request_mapping_path}/")
public class ODataController {

    private TokenParser tokenParser;
    private JPAODataSessionContextAccess serviceContext;

    private static final Logger logger = LoggerFactory.getLogger(ODataController.class);
    private final String PUBLIC_VISIBILITY = "public";
    private final String INTERNAL_VISIBILITY = "internal";
    private final String PRIVATE_VISIBILITY = "private";
    private final String DEFAULT_EMPTY_ID_CLAIM = "eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee";
    private final String DEFAULT_TENANT_ID = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb";

    public ODataController(@Autowired final TokenParser tokenParser, @Autowired final JPAODataSessionContextAccess serviceContext) {
        super();
        this.tokenParser = tokenParser;
        this.serviceContext = serviceContext;
    }

    @GetMapping(value = "**")
    public void handleODataRequest(HttpServletRequest request, HttpServletResponse response) throws ODataException, IOException {

        Token token = tokenParser.fromRequest(request);
        String tenantId = token == null ? "" : token.extractTenant();
        // store the tenant id, so it is available to post-OData processing filters
        request.setAttribute(Common.REQUEST_ATTRIBUTE_TENANT_ID, tenantId);
        final JPAODataRequestContext requestContext = createRequestContext(createClaims(token, tenantId), tenantId);
        new JPAODataRequestHandler(serviceContext, requestContext).process(request, response);
    }

    private JPAODataClaimsProvider createClaims(final Token token, String tenantID) throws IOException {
        final JPAODataClaimsProvider claims = new JPAODataClaimsProvider();

        if (token == null) {
            logger.warn("Could not determine claims because token is null");
            return claims;
        }

        if (tenantID.equals(Token.EMPTY_TENANT_DUE_TO_MISSING_CONSUMER_ID)) {
            return claims;
        }

        if (tenantID == null || tenantID.isEmpty()) {
            if (token.applicationTenantId != null && !token.applicationTenantId.isEmpty()) {
                logger.warn("Application tenant ID is provided through header and tenant ID is empty");
            } else {
                logger.warn("Could not determine tenant claim");
                return claims;
            }
        }

        final JPAClaimsPair<UUID> destinationTenantJPAPair;
        if (tenantID == null || tenantID.isEmpty()) {
            destinationTenantJPAPair = new JPAClaimsPair<>(UUID.fromString(DEFAULT_TENANT_ID));
        } else {
            destinationTenantJPAPair = new JPAClaimsPair<>(UUID.fromString(tenantID));
        }

        String callerID = token.getCallerID();
        if (callerID == null || callerID.isEmpty()) {
            claims.add("caller_id",new JPAClaimsPair<>(UUID.fromString(DEFAULT_EMPTY_ID_CLAIM))); // avoid returning misleading claims error
        } else {
            claims.add("caller_id",new JPAClaimsPair<>(UUID.fromString(callerID)));
        }

        boolean shouldUseDefaultTenant = true;
        if (token.getFormationIDsClaims().isEmpty()) {
            logger.warn("Could not determine formation claim");
            claims.add("formation_scope", new JPAClaimsPair<>(UUID.fromString(DEFAULT_EMPTY_ID_CLAIM))); // in the consumer-provider flow, if there are currently no formations the rtCtx is part of; we will return empty array this way instead of misleading claims error
        } else {
            for (String formationID : token.getFormationIDsClaims()) {
                if (formationID.equals(Token.DEFAULT_FORMATION_ID_CLAIM)) {
                    shouldUseDefaultTenant = false; // if the DEFAULT_FORMATION_ID_CLAIM is present it means that we will be filtering by tenant_id, so we don't want to use the default tenant_id
                }
                claims.add("formation_scope", new JPAClaimsPair<>(UUID.fromString(formationID)));
            }
        }

        final JPAClaimsPair<UUID> tenantIDJPAPair;
        if (shouldUseDefaultTenant) {
            tenantIDJPAPair = new JPAClaimsPair<>(UUID.fromString(DEFAULT_TENANT_ID));
        } else {
            tenantIDJPAPair = new JPAClaimsPair<>(UUID.fromString(tenantID));
        }
        claims.add("tenant_id", tenantIDJPAPair);
        claims.add("destination_tenant_id", destinationTenantJPAPair);

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

    private JPAODataRequestContext createRequestContext(JPAODataClaimsProvider claimsProvider, String tenantId) {
        return JPAODataRequestContext.with()
                                     .setCUDRequestHandler(new JPAExampleCUDRequestHandler())
                                     .setDebugSupport(new DefaultDebugSupport())
                                     .setClaimsProvider(claimsProvider)
                                     .setParameter(Common.REQUEST_ATTRIBUTE_TENANT_ID, tenantId)
                                     .build();
    }
}
