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
    private static final String RESPONSE_ROOT_PROPERTY_NAME = "destinations";

    private String reloadUrl;
    private String sensitiveDataUrl;

    private String tenantHeaderName;
    private String correlationIdHeaderName;
    private String sensitiveDataQueryParamName;

    private Path authTokenPath;
    private RestTemplate restTemplate;


    public DestinationFetcherClient(String reloadUrl, String sensitiveDataUrl,
        String tenantHeaderName, String correlationIdHeaderName, String sensitiveDataQueryParamName,
        String authTokenPath, RestTemplate restTemplate) {

        this.reloadUrl = reloadUrl;
        this.sensitiveDataUrl = sensitiveDataUrl;
        this.tenantHeaderName = tenantHeaderName;
        this.correlationIdHeaderName = correlationIdHeaderName;
        this.sensitiveDataQueryParamName = sensitiveDataQueryParamName;
        this.authTokenPath = Path.of(authTokenPath);
        this.restTemplate = restTemplate;
    }

    public void reload(String tenantId, String correlationId) throws IOException {
        restTemplate.exchange(this.reloadUrl, HttpMethod.PUT,
            new HttpEntity<>(prepareRequestHeaders(tenantId, correlationId)), String.class);
    }

    public ObjectNode getDestinations(String tenantId, String correlationId,
        List<String> destinationNames) throws IOException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(sensitiveDataUrl);
        for (String destName : destinationNames) {
            builder.queryParam(sensitiveDataQueryParamName, destName);
        }
        String uriString = builder.build().toUriString();

        ResponseEntity<ObjectNode> response = restTemplate.exchange(uriString, HttpMethod.GET,
            new HttpEntity<>(prepareRequestHeaders(tenantId, correlationId)), ObjectNode.class);

        return (ObjectNode) response.getBody().get(RESPONSE_ROOT_PROPERTY_NAME);
    }

    private HttpHeaders prepareRequestHeaders(String tenantId, String correlationId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        setAuthorizationHeader(headers);
        headers.set(tenantHeaderName, tenantId);
        headers.set(correlationIdHeaderName, correlationId);
        return headers;
    }

    private void setAuthorizationHeader(HttpHeaders headers) throws IOException {
        String token = Files.readString(authTokenPath);
        headers.setBearerAuth(token.strip());
    }

}
