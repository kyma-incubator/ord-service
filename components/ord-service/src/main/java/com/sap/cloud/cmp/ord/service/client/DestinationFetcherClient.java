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

import com.fasterxml.jackson.databind.node.ObjectNode;

public class DestinationFetcherClient {
    private static final String REQUEST_QUERY_DESTINATION_NAME = "name";

    private static final String RESPONSE_ROOT_PROPERTY_NAME = "destinations";

    private Path authTokenPath;
    private String reloadUrl;
    private String sensitiveDataUrl;
    private String tenantHeader;
    private RestTemplate restTemplate;


    public DestinationFetcherClient(String reloadUrl, String sensitiveDataUrl, String tenantHeader, String authTokenPath, RestTemplate restTemplate) {
        this.authTokenPath = Path.of(authTokenPath);
        this.reloadUrl = reloadUrl;
        this.sensitiveDataUrl = sensitiveDataUrl;
        this.tenantHeader = tenantHeader;
        this.restTemplate = restTemplate;
    }

    public void reload(String tenantId) throws IOException {
        restTemplate.exchange(this.reloadUrl, HttpMethod.PUT,
            new HttpEntity<>(prepareRequestHeaders(tenantId)), String.class);
    }

    public ObjectNode getDestinations(String tenantId, List<String> destinationNames) throws IOException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(sensitiveDataUrl);
        for (String destName : destinationNames) {
            builder.queryParam(REQUEST_QUERY_DESTINATION_NAME, destName);
        }
        String uriString = builder.build().toUriString();

        ResponseEntity<ObjectNode> response = restTemplate.exchange(uriString, HttpMethod.GET,
            new HttpEntity<>(prepareRequestHeaders(tenantId)), ObjectNode.class);

        return (ObjectNode) response.getBody().get(RESPONSE_ROOT_PROPERTY_NAME);
    }

    private HttpHeaders prepareRequestHeaders(String tenantId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        setAuthorizationHeader(headers);
        headers.set(tenantHeader, tenantId);
        return headers;
    }

    private void setAuthorizationHeader(HttpHeaders headers) throws IOException {
        String token = Files.readString(authTokenPath);
        headers.setBearerAuth(token.strip());
    }

}
