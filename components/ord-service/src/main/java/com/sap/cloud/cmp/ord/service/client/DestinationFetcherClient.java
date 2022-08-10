package com.sap.cloud.cmp.ord.service.client;

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

    private String reloadUrl;
    private String sensitiveDataUrl;
    private String userContextHeader;
    private RestTemplate restTemplate;


    public DestinationFetcherClient(String reloadUrl, String sensitiveDataUrl, String userContextHeader, RestTemplate restTemplate) {
        this.reloadUrl = reloadUrl;
        this.sensitiveDataUrl = sensitiveDataUrl;
        this.userContextHeader = userContextHeader;
        this.restTemplate = restTemplate;
    }

    public void reload(String subaccount) throws JsonProcessingException {
        // TODO: this request changes the state of the server, a different method would be more appropriate
        // TODO: also revisit the paths of the destinations fetcher
        restTemplate.exchange(this.reloadUrl, HttpMethod.GET,
            new HttpEntity<>(prepareRequestHeaders(subaccount)), String.class);
    }

    public ObjectNode getDestinations(String subaccount, List<String> destinationNames) throws JsonProcessingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(sensitiveDataUrl);
        builder.queryParam(REQUEST_QUERY_DESTINATION_NAMES, "[" + String.join(",", destinationNames) + "]");
        String uriString = builder.build().toUriString();
        // TODO: a more standard alternative for passing several values
        // is providing the query parameter multiple times
        ResponseEntity<ObjectNode> response = restTemplate.exchange(uriString, HttpMethod.GET,
            new HttpEntity<>(prepareRequestHeaders(subaccount)), ObjectNode.class);

        return (ObjectNode) response.getBody().get(RESPONSE_ROOT_PROPERTY_NAME);
    }

    private HttpHeaders prepareRequestHeaders(String subaccount) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        setUserContextHeader(headers, subaccount);
        return headers;
    }

    private void setUserContextHeader(HttpHeaders headers, String subaccount) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userContext = mapper.createObjectNode();
        userContext.put(REQUEST_USER_CONTEXT_SUBACCOUNT_ID, subaccount);
        headers.set(userContextHeader, mapper.writeValueAsString(userContext));
    }

}
