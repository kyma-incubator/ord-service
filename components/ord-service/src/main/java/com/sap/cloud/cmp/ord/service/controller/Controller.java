package com.sap.cloud.cmp.ord.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.util.Pair;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;

public abstract class Controller {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final String TENANTS_MAP_KEY = "tenant";
    private final String CONSUMER_TENANT_KEY = "consumerTenant";
    private final String PROVIDER_TENANT_KEY = "providerTenant";
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String JWT_TOKEN_SPLIT_PARTS = "\\.";
    private final int PAYLOAD_INDEX = 1;

    Pair<String, String> extractTenantsFromIDToken(final HttpServletRequest request) throws IOException {
        final String idToken = request.getHeader(AUTHORIZATION_HEADER);
        Pair<String, String> tenantsPair = null;

        if (idToken != null && !idToken.isEmpty()) {
            // The id_token comes with `Bearer` prefix which we should trim
            String idTokenStripped = idToken.substring(idToken.indexOf(" ") + 1);

            String[] splitIDToken = idTokenStripped.split(JWT_TOKEN_SPLIT_PARTS);
            String base64EncodedPayload = splitIDToken[PAYLOAD_INDEX];

            byte[] idTokenBytes = Base64.getDecoder().decode(base64EncodedPayload);
            String idTokenDecoded = new String(idTokenBytes);

            JsonNode tokenTree = mapper.readTree(idTokenDecoded);
            String unescapedTenants = tokenTree.get(TENANTS_MAP_KEY).asText().replace("\\", "");

            JsonNode tenantsTree = mapper.readTree(unescapedTenants);

            String tenantID = tenantsTree.path(CONSUMER_TENANT_KEY).asText();
            String providerTenantID = tenantsTree.path(PROVIDER_TENANT_KEY).asText();
            if (providerTenantID.isEmpty()) {
                providerTenantID = tenantID;
            }
            tenantsPair = Pair.of(tenantID, providerTenantID);
        }

        return tenantsPair;
    }
}
