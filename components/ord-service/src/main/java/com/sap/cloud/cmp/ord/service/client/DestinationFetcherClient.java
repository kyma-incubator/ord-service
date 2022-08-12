package com.sap.cloud.cmp.ord.service.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DestinationFetcherClient {
    private static final String REQUEST_USER_CONTEXT_SUBACCOUNT_ID = "subaccountId";
    private static final String REQUEST_QUERY_DESTINATION_NAMES = "name";

    private static final String RESPONSE_ROOT_PROPERTY_NAME = "destinations";

    private Path authTokenPath;
    private String reloadUrl;
    private String sensitiveDataUrl;
    private String userContextHeader;
    private RestTemplate restTemplate;


    public DestinationFetcherClient(String reloadUrl, String sensitiveDataUrl, String userContextHeader, String authTokenPath, RestTemplate restTemplate) {
        this.authTokenPath = Path.of(authTokenPath);
        this.reloadUrl = reloadUrl;
        this.sensitiveDataUrl = sensitiveDataUrl;
        this.userContextHeader = userContextHeader;
        this.restTemplate = restTemplate;
    }

    public void reload(String subaccount) throws IOException {
        restTemplate.exchange(this.reloadUrl, HttpMethod.PUT,
            new HttpEntity<>(prepareRequestHeaders(subaccount)), String.class);
    }

    public ObjectNode getDestinations(String subaccount, List<String> destinationNames) throws IOException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(sensitiveDataUrl);
        builder.queryParam(REQUEST_QUERY_DESTINATION_NAMES, "[" + String.join(",", destinationNames) + "]");
        String uriString = builder.build().toUriString();
        ResponseEntity<ObjectNode> response = restTemplate.exchange(uriString, HttpMethod.GET,
            new HttpEntity<>(prepareRequestHeaders(subaccount)), ObjectNode.class);

        return (ObjectNode) response.getBody().get(RESPONSE_ROOT_PROPERTY_NAME);
    }

    private HttpHeaders prepareRequestHeaders(String subaccount) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        setAuthorizationHeader(headers);
        setUserContextHeader(headers, subaccount);
        return headers;
    }

    private void setAuthorizationHeader(HttpHeaders headers) throws IOException {
        String token = Files.readString(authTokenPath);
        headers.setBearerAuth(token.strip());
    }

    private void setUserContextHeader(HttpHeaders headers, String subaccount) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userContext = mapper.createObjectNode();
        userContext.put(REQUEST_USER_CONTEXT_SUBACCOUNT_ID, subaccount);
        headers.set(userContextHeader, mapper.writeValueAsString(userContext));
    }

}
