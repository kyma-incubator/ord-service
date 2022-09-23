package com.sap.cloud.cmp.ord.service.destinations;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import com.sap.cloud.cmp.ord.service.client.DestinationFetcherClient;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DestinationFetcherClientTest {

    private static final String TENANT = "3e64ebae-38b5-46a0-b1ed-9ccee153a0ae";
    private static final String X_REQUEST_ID = "xreqebae-38b5-46a0-b1ed-9ccee153a0ae";

    @Value("${destination_fetcher.sensitive_data_url}")
    private String sensitiveDataUrl;
    @Value("${destination_fetcher.reload_url}")
    private String reloadUrl;
    @Value("${destination_fetcher.tenant_header}")
    private String tenantHeader;
    @Value("${destination_fetcher.x_request_id_header}")
    private String xRequestIDHeader;
    @Value("${destination_fetcher.sensitive_data_query_param}")
    private String sensitiveDataQueryParam;
    @Value("${destination_fetcher.auth_token_path}")
    private String authTokePath;
    RestTemplate restTemplate;

    DestinationFetcherClient client;

    private MockRestServiceServer mockServer;

    @Before
    public void setup() {
        this.restTemplate = new RestTemplate();
        this.client = new DestinationFetcherClient(reloadUrl, sensitiveDataUrl, tenantHeader, xRequestIDHeader,
                sensitiveDataQueryParam, authTokePath, restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testReload_CallsReloadUrlAndDoesNotThrowException_WhenReceivedStatusOK() throws Exception {
        mockServer.expect(once(), requestTo(reloadUrl))
                .andExpect(header(tenantHeader, TENANT))
                .andExpect(header(xRequestIDHeader, X_REQUEST_ID))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));

        client.reload(TENANT, X_REQUEST_ID);
        mockServer.verify();
    }

    @Test
    public void testGetDestinations_CallsSensitiveDataUrlWithDestinationQueryParameter_WhenStatusIsOK()
            throws Exception {

        String expectedDest1 = "\"dest1Info\"";
        String expectedDest2 = "\"dest2Info\"";
        List<String> destinations = new ArrayList<>();
        destinations.add("dest1");
        destinations.add("dest2");

        mockServer.expect(once(), request ->
                        assertEquals(sensitiveDataUrl, request.getURI().toString().split("\\?")[0]))
                .andExpect(request -> assertEquals("name=dest1&name=dest2",
                        request.getURI().toString().split("\\?")[1]))
                .andExpect(header(tenantHeader, TENANT))
                .andExpect(header(xRequestIDHeader, X_REQUEST_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"destinations\": {\"dest1\":"+expectedDest1+", \"dest2\":"+expectedDest2+"}}",
                        MediaType.APPLICATION_JSON));

        ObjectNode result = client.getDestinations(TENANT, X_REQUEST_ID, destinations);

        assertEquals(expectedDest1, result.get("dest1").toString());
        assertEquals(expectedDest2, result.get("dest2").toString());
        mockServer.verify();
    }
}
