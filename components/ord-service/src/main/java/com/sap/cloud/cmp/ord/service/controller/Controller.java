package com.sap.cloud.cmp.ord.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;

public abstract class Controller {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final String TENANT_KEY = "tenant";
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String JWT_TOKEN_SPLIT_PARTS = "\\.";
    private final int PAYLOAD_INDEX = 1;

    String extractInternalTenantIdFromIDToken(final HttpServletRequest request) throws IOException {
        final String idToken = request.getHeader(AUTHORIZATION_HEADER);
        String tenantID = null;

        if (idToken != null && !idToken.isEmpty()) {
            // The id_token comes with `Bearer` prefix which we should trim
            String idTokenStripped = idToken.substring(idToken.indexOf(" ") + 1);

            String[] splitIDToken = idTokenStripped.split(JWT_TOKEN_SPLIT_PARTS);
            String base64EncodedPayload = splitIDToken[PAYLOAD_INDEX];

            byte[] idTokenBytes = Base64.getDecoder().decode(base64EncodedPayload);
            String idTokenDecoded = new String(idTokenBytes);

            JsonNode parent = mapper.readTree(idTokenDecoded);
            tenantID = parent.path(TENANT_KEY).asText();
        }
        return tenantID;
    }
}
