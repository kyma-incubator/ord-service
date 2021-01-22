package com.sap.cloud.cmp.ord.service.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/${odata.jpa.request_mapping_path}/**")
public class ODataController {

    private final String TENANT_KEY = "tenant";
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String JWT_TOKEN_SPLIT_PARTS = "\\.";
    private final int PAYLOAD = 1;

    @Autowired
    private JPAODataCRUDContextAccess serviceContext;

    @RequestMapping(value = "**", method = { RequestMethod.GET })
    @ResponseBody
    public void handleODataRequest(HttpServletRequest request, HttpServletResponse response) throws ODataException, IOException {
            final JPAODataGetHandler handler = new JPAODataGetHandler(serviceContext);
            handler.getJPAODataRequestContext().setDebugSupport(new DefaultDebugSupport()); // Use query parameter odata-debug=json to activate.

            handler.getJPAODataRequestContext().setClaimsProvider(createClaims(request));
            handler.process(request, response);
    }

    private JPAODataClaimsProvider createClaims(final HttpServletRequest req) throws IOException {
        final String idToken = req.getHeader(AUTHORIZATION_HEADER);

        final JPAODataClaimsProvider claims = new JPAODataClaimsProvider();
        if (idToken != null && !idToken.isEmpty()) {
            String tenantID = extractInternalTenantIdFromIDToken(idToken);

            final JPAClaimsPair<UUID> user = new JPAClaimsPair<>(UUID.fromString(tenantID));
            claims.add("tenant_id", user);
        }
        return claims;
    }

    private String extractInternalTenantIdFromIDToken (String encodedIDToken) throws IOException {
        // The id_token comes with `Bearer` prefix which we should trim
        String idTokenStripped = encodedIDToken.substring(encodedIDToken.indexOf(" ") + 1);

        String[] splitIDToken = idTokenStripped.split(JWT_TOKEN_SPLIT_PARTS);
        String base64EncodedPayload = splitIDToken[PAYLOAD];

        byte[] idTokenBytes = Base64.getDecoder().decode(base64EncodedPayload);
        String idTokenDecoded = new String(idTokenBytes);

        JsonNode parent= new ObjectMapper().readTree(idTokenDecoded);
        String tenantID = parent.path(TENANT_KEY).asText();

        return tenantID;
    }
}
