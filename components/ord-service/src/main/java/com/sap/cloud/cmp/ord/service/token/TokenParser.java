package com.sap.cloud.cmp.ord.service.token;

import jakarta.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TokenParser {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String APPLICATION_TENANT_ID_HEADER_KEY = "applicationTenantId";

    private SubscriptionHelper subscriptionHelper;

    public TokenParser(SubscriptionHelper subscriptionHelper) {
        this.subscriptionHelper = subscriptionHelper;
    }

    public Token fromRequest(final HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
        final String idToken = request.getHeader(AUTHORIZATION_HEADER);
        if (idToken == null || idToken.isEmpty()) {
            return null;
        }

        final String applicationTenantId = request.getHeader(APPLICATION_TENANT_ID_HEADER_KEY);

        return new Token(subscriptionHelper, idToken, applicationTenantId);
    }
}
