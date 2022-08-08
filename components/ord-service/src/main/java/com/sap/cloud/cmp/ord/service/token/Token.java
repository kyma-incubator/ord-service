package com.sap.cloud.cmp.ord.service.token;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.cmp.ord.service.repository.SelfRegisteredRuntimeRepository;

public class Token {
    private static final Logger logger = LoggerFactory.getLogger(Token.class);

    private static final String JWT_TOKEN_SPLIT_PARTS = "\\.";
    private static final int PAYLOAD_INDEX = 1;

    private static final String TENANTS_MAP_KEY = "tenant";
    private static final String CONSUMER_TENANT_KEY = "consumerTenant";
    private static final String PROVIDER_TENANT_KEY = "providerTenant";

    private static final String TOKEN_CLIENT_ID_KEY = "tokenClientID";
    private static final String TOKEN_REGION_KEY = "region";

    private static final String SCOPES_KEY = "scopes";
    private final String INTERNAL_VISIBILITY_SCOPE = "internal_visibility:read";

    private static final ObjectMapper mapper = new ObjectMapper();

    private SubscriptionHelper subscriptionHelper;
    private JsonNode content;

    public Token(SubscriptionHelper subscriptionHelper, String idTokenEncoded) throws JsonMappingException, JsonProcessingException {
        this.subscriptionHelper = subscriptionHelper;
        String idTokenDecoded = decodeIDToken(idTokenEncoded);
        this.content = mapper.readTree(idTokenDecoded);
    }

    public String extractTenant() {
        String unescapedTenants = content.get(TENANTS_MAP_KEY).asText().replace("\\", "");

        String providerTenantID = "";
        try {
            JsonNode tenantsTree = mapper.readTree(unescapedTenants);

            String tenant = tenantsTree.path(CONSUMER_TENANT_KEY).asText();
            providerTenantID = tenantsTree.path(PROVIDER_TENANT_KEY).asText();
            if (providerTenantID == null || providerTenantID.isEmpty() || providerTenantID.equals(tenant)) {
                return tenant;
            }

            logger.info("Both consumer {} and provider {} tenants are present. Checking if there is a subscription...", tenant, providerTenantID);
            String tokenClientId = content.get(TOKEN_CLIENT_ID_KEY).asText();
            if (tokenClientId == null || tokenClientId.isEmpty()) {
                logger.error("could not find consumer token client ID");
                return "";
            }

            String tokenPrefix = subscriptionHelper.getTokenPrefix();
            if (tokenClientId.startsWith(tokenPrefix)){
                tokenClientId = tokenClientId.substring(tokenPrefix.length());
            }

            String tokenRegion = content.get(TOKEN_REGION_KEY).asText();
            if (tokenRegion == null || tokenRegion.isEmpty()) {
                logger.error("could not determine token's region");
                return "";
            }

            SelfRegisteredRuntimeRepository repo = subscriptionHelper.getRepo();
            Set<String> runtimeIds = repo.findSelfRegisteredRuntimesByLabels(providerTenantID, subscriptionHelper.getSelfRegKey(),
                tokenClientId, subscriptionHelper.getRegionKey(), tokenRegion);
            for (String runtimeId : runtimeIds) {
                if (repo.isRuntimeSubscriptionAvailableInTenant(tenant, runtimeId) > 0) {
                    return tenant;
                }
            }
        } catch (IOException ignored) {}

        return providerTenantID;
    }

    public boolean isInternalVisibilityScopePresent() {
        String scopes = content.get(SCOPES_KEY).asText();
        return scopes.contains(INTERNAL_VISIBILITY_SCOPE);
    }

    private String decodeIDToken(String idTokenEncoded) {
        // The id_token comes with `Bearer` prefix which we should trim
        String idTokenStripped = idTokenEncoded.substring(idTokenEncoded.indexOf(" ") + 1);

        String[] splitIDToken = idTokenStripped.split(JWT_TOKEN_SPLIT_PARTS);
        String base64EncodedPayload = splitIDToken[PAYLOAD_INDEX];

        byte[] idTokenBytes = Base64.getDecoder().decode(base64EncodedPayload);

        return new String(idTokenBytes);
    }
}
