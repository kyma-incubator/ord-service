package com.sap.cloud.cmp.ord.service.destinations;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import com.sap.cloud.cmp.ord.service.token.SubscriptionHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sap.cloud.cmp.ord.service.client.DestinationFetcherClient;
import com.sap.cloud.cmp.ord.service.controller.ODataController;
import com.sap.cloud.cmp.ord.service.filter.DestinationForceReloadFilter;
import com.sap.cloud.cmp.ord.service.filter.DestinationSensitiveDataFilter;
import com.sap.cloud.cmp.ord.service.token.Token;
import com.sap.cloud.cmp.ord.service.token.TokenParser;
import com.sap.olingo.jpa.processor.core.api.JPAODataRequestHandler;
import com.sap.olingo.jpa.processor.core.api.JPAODataSessionContextAccess;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DestinationFetcherClient.class, properties = "spring.main.lazy-initialization=true")
public class FetchingDestinationsTest {

    private static final String TOKEN_VALUE = "eyAiYWxnIjogIlJTMjU2IiwgImtpZCI6ICI4NTQ3NzFlMi00MGFlLTQyMzctOTcwNi1kMTg5NDc2M2Y4N2IiLCAidHlwIjogIkpXVCIgfQo.eyAiY29uc3VtZXJzIjogIlt7XCJDb25zdW1lcklEXCI6XCJhZG1pblwiLFwiQ29uc3VtZXJUeXBlXCI6XCJTdGF0aWMgVXNlclwiLFwiRmxvd1wiOlwiSldUXCJ9XSIsICJjb25zdW1lcklEIjogInRlc3RDb25zdW1lcklEIiwgImV4cCI6IDE2MzMxMDE1MjksICJpYXQiOiAxNjMzMDk3OTI5LCAiaXNzIjogImh0dHBzOi8vb2F0aGtlZXBlci5reW1hLmxvY2FsLyIsICJqdGkiOiAiODU2Y2Q1ZTgtZDg3MS00MzkxLTgwMjktMjg2ZGNlNzIzNGNmIiwgIm5iZiI6IDE2MzMwOTc5MjksICJzY29wZXMiOiAiYXBwbGljYXRpb246cmVhZCBhcHBsaWNhdGlvbjp3cml0ZSBhcHBsaWNhdGlvbl90ZW1wbGF0ZTpyZWFkIGFwcGxpY2F0aW9uX3RlbXBsYXRlOndyaXRlIGludGVncmF0aW9uX3N5c3RlbTpyZWFkIGludGVncmF0aW9uX3N5c3RlbTp3cml0ZSBydW50aW1lOnJlYWQgcnVudGltZTp3cml0ZSBsYWJlbF9kZWZpbml0aW9uOnJlYWQgbGFiZWxfZGVmaW5pdGlvbjp3cml0ZSBldmVudGluZzptYW5hZ2UgdGVuYW50OnJlYWQgYXV0b21hdGljX3NjZW5hcmlvX2Fzc2lnbm1lbnQ6cmVhZCBhdXRvbWF0aWNfc2NlbmFyaW9fYXNzaWdubWVudDp3cml0ZSBhcHBsaWNhdGlvbi5hdXRoczpyZWFkIGFwcGxpY2F0aW9uLndlYmhvb2tzOnJlYWQgYXBwbGljYXRpb25fdGVtcGxhdGUud2ViaG9va3M6cmVhZCBidW5kbGUuaW5zdGFuY2VfYXV0aHM6cmVhZCBkb2N1bWVudC5mZXRjaF9yZXF1ZXN0OnJlYWQgZXZlbnRfc3BlYy5mZXRjaF9yZXF1ZXN0OnJlYWQgYXBpX3NwZWMuZmV0Y2hfcmVxdWVzdDpyZWFkIGludGVncmF0aW9uX3N5c3RlbS5hdXRoczpyZWFkIHJ1bnRpbWUuYXV0aHM6cmVhZCBmZXRjaC1yZXF1ZXN0LmF1dGg6cmVhZCB3ZWJob29rcy5hdXRoOnJlYWQgaW50ZXJuYWxfdmlzaWJpbGl0eTpyZWFkIiwgInN1YiI6ICJDaUJyZDNsM2J6VTFjWFl6TVRkaE1YazFiM0psY1RkalozWnFjek0yTVcxdGJ4SUZiRzlqWVd3IiwgInRlbmFudCI6IntcImNvbnN1bWVyVGVuYW50XCI6XCIzZTY0ZWJhZS0zOGI1LTQ2YTAtYjFlZC05Y2NlZTE1M2EwYWVcIixcImV4dGVybmFsVGVuYW50XCI6XCIzZTY0ZWJhZS0zOGI1LTQ2YTAtYjFlZC05Y2NlZTE1M2EwYWVcIn0iIH0K.";
    private static final String TENANT = "3e64ebae-38b5-46a0-b1ed-9ccee153a0ae";
    private static final String X_REQUEST_ID = "xreqebae-38b5-46a0-b1ed-9ccee153a0ae";

    @Value("${odata.jpa.request_mapping_path}")
    private String requestMappingPath;
    @Value("${http.headers.correlationId}")
    private String correlationIdHeader;

    private MockMvc mvc;

    @Mock
    private TokenParser tokenParser;

    @Mock
    private JPAODataSessionContextAccess serviceContext;

    @Mock
    private DestinationFetcherClient destsFetcherClient;

    @Mock
    private SubscriptionHelper subscriptionHelper;

    @InjectMocks
    private ODataController odataController;

    @InjectMocks
    private DestinationForceReloadFilter reloadFilter;

    @InjectMocks
    private DestinationSensitiveDataFilter sensitiveDataFilter;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(reloadFilter, "odataPath", requestMappingPath);
        ReflectionTestUtils.setField(sensitiveDataFilter, "odataPath", requestMappingPath);
        ReflectionTestUtils.setField(reloadFilter, "correlationIdHeader", correlationIdHeader);
        ReflectionTestUtils.setField(sensitiveDataFilter, "correlationIdHeader", correlationIdHeader);

        mvc = MockMvcBuilders.standaloneSetup(odataController)
                .addPlaceholderValue("odata.jpa.request_mapping_path", requestMappingPath)
                .addPlaceholderValue("http.headers.correlationId", correlationIdHeader)
                .addFilters(reloadFilter)
                .addFilters(sensitiveDataFilter)
                .build();
    }

    @After
    public void cleanup() {
        reset(tokenParser);
        reset(destsFetcherClient);
    }

    @Test
    public void testReloadFilter_DoesNotCallDestinationFetcher_WhenRequestIsNotOData() throws Exception {
        TestLogic testLogic = () -> {
            mvc.perform(get("/not/odata"))
                .andExpect(status().isNotFound());

            verify(destsFetcherClient, never()).reload(anyString(), anyString());
        };

        runTest(null, ResponseType.JSON, testLogic);
    }

    @Test
    public void testReloadFilter_DoesNotCallDestinationFetcher_WhenRealoadQueryParamIsNotProvided() throws Exception {
        String odataResponse = "{}";
        TestLogic testLogic = () -> {
            mvc.perform(
                get(requestPath(Reload.FALSE, ResponseType.JSON)))
                .andExpect(status().isOk())
                .andExpect(content().string(odataResponse));

            verify(destsFetcherClient, never()).reload(anyString(), anyString());
        };

        runTest(odataResponse, ResponseType.JSON, testLogic);
    }

    @Test
    public void testReloadFilter_ReturnsUnauthorized_WhenTokenIsNotProvided() throws Exception {
        when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(null);

        TestLogic testLogic = () -> {
            mvc.perform(
                get(requestPath(Reload.TRUE, ResponseType.JSON)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(HttpStatus.UNAUTHORIZED.getReasonPhrase()));
        };

        runTest(null, ResponseType.JSON, testLogic);
    }

    @Test
    public void testReloadFilter_ReturnsInternalServerError_WhenCallToDestinationFetcherFails() throws Exception {
        when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(new Token(subscriptionHelper, TOKEN_VALUE, null));
        doThrow(new RestClientResponseException("Request failed",
            HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
            null, null, null)
        ).when(destsFetcherClient).reload(TENANT, X_REQUEST_ID);

        TestLogic testLogic = () -> {
            mvc.perform(
                get(requestPath(Reload.TRUE, ResponseType.JSON)).header(correlationIdHeader, X_REQUEST_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        };

        runTest(null, ResponseType.JSON, testLogic);
    }

    @Test
    public void testReloadFilter_ReturnsODataResponse_WhenCallToDestinationFetcherSucceeds() throws Exception {
        String odataResponse = "{}";
        when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(new Token(null, TOKEN_VALUE, null)); // todo::: adapt

        TestLogic testLogic = () -> {
            mvc.perform(
                get(requestPath(Reload.TRUE, ResponseType.JSON)))
                .andExpect(status().isOk())
                .andExpect(content().string(odataResponse));
        };

        runTest(odataResponse, ResponseType.JSON, testLogic);
    }

    @Test
    public void testSensitiveDataFilter_DoesNotCallDestinationFetcher_WhenRequestIsNotOData() throws Exception {
        TestLogic testLogic = () -> {
            mvc.perform(get("/not/odata"))
                .andExpect(status().isNotFound());

            verify(destsFetcherClient, never()).getDestinations(anyString(), anyString(), anyList());
        };

        runTest(null, ResponseType.JSON, testLogic);
    }

    @Test
    public void testSensitiveDataFilter_DoesNotCallDestinationFetcher_WhenDestinationsAreNotRequested() throws Exception {
        String odataResponse = "{}";
        TestLogic testLogic = () -> {
            mvc.perform(get("/" + requestMappingPath + "/systemInstances?$expand=consumptionBundles"))
                .andExpect(status().isOk())
                .andExpect(content().string(odataResponse)
            );

            verify(destsFetcherClient, never()).getDestinations(anyString(), anyString(), anyList());
        };

        runTest(odataResponse, ResponseType.JSON, testLogic);
    }

    @Test
    public void testSensitiveDataFilter_DoesNotCallDestinationFetcher_WhenSensitiveDataIsNotRequested() throws Exception {
        String odataResponse =
        "{" +
            "\"value\": [{" +
                "\"consumptionBundles\": [{" +
                    "\"destinations\": [{" +
                        "\"name\": \"dest-1\"" +
                    "}]" +
                "}]" +
            "}]" +
        "}";

        TestLogic testLogic = () -> {
            mvc.perform(get("/" + requestMappingPath + "/systemInstances?$expand=consumptionBundles($expand=destinations($select=name))"))
                .andExpect(status().isOk())
                .andExpect(content().string(odataResponse)
            );

            verify(destsFetcherClient, never()).getDestinations(anyString(), anyString(), anyList());
        };

        runTest(odataResponse, ResponseType.JSON, testLogic);
    }

    @Test
    public void testSensitiveDataFilter_ReturnsInternalServerError_WhenCallToDestinationFetcherFails() throws Exception {
        when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(new Token(subscriptionHelper, TOKEN_VALUE, null));

        String odataResponse =
        "{" +
            "\"value\": [{" +
                "\"consumptionBundles\": [{" +
                    "\"destinations\": [{" +
                        "\"name\": \"dest-1\"," +
                        "\"sensitiveData\": \"__sensitive_data__dest-1__sensitive_data__\"" +
                    "}]" +
                "}]" +
            "}]" +
        "}";

        when(destsFetcherClient.getDestinations(TENANT, X_REQUEST_ID, Arrays.asList("dest-1"))).thenThrow(new RestClientResponseException("Request failed",
        HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        null, null, null));


        TestLogic testLogic = () -> {
            mvc.perform(get(requestPath(Reload.FALSE, ResponseType.XML)).header(correlationIdHeader, X_REQUEST_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            );
        };

        runTest(odataResponse, ResponseType.JSON, testLogic);
    }

     @Test
     public void testSensitiveDataFilter_ReturnsDestinationsWithSensitiveDataWhereAvailableInXML() throws Exception {
         when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(new Token(subscriptionHelper, TOKEN_VALUE, null));

         String odataResponse =
         "<feed>" +
             "<entry>" +
                 "<id>system-1</id>" +
                 "<link>" +
                     "<entry>" +
                         "<id>my-bundle</id>" +
                         "<link>" +
                             "<entry>" +
                                 "<name>dest-1</name>" +
                                 "<sensitiveData>__sensitive_data__dest-1__sensitive_data__</sensitiveData>" +
                             "</entry>" +
                             "<entry>" +
                                 "<name>dest-2</name>" +
                                 "<sensitiveData>__sensitive_data__dest-2__sensitive_data__</sensitiveData>" +
                             "</entry>" +
                         "</link>" +
                     "</entry>" +
                 "</link>" +
             "</entry>" +
         "</feed>";

         String destsFetcherResponse =
         "{" +
             "\"dest-1\": {" +
                 "\"password\": \"super-secret\"" +
             "}" +
         "}";

         when(destsFetcherClient.getDestinations(eq(TENANT), eq(X_REQUEST_ID), anyList())).thenReturn((ObjectNode) new ObjectMapper().readTree(destsFetcherResponse));


         TestLogic testLogic = () -> {
             mvc.perform(get(requestPath(Reload.FALSE, ResponseType.XML)).header(correlationIdHeader, X_REQUEST_ID))
                 .andExpect(status().isOk())
                 .andExpect(content().string(allOf(
                     containsString("<name>dest-1</name><sensitiveData><password>super-secret</password>"),
                     containsString("<name>dest-2</name><sensitiveData></sensitiveData>")
                 ))
             );
         };

         runTest(odataResponse, ResponseType.XML, testLogic);
     }

    @Test
    public void testSensitiveDataFilter_ReturnsDestinationsWithSensitiveDataWhereAvailableInJSON() throws Exception {
        when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(new Token(subscriptionHelper, TOKEN_VALUE, null));

        String odataResponse =
        "{" +
            "\"value\": [{" +
                "\"consumptionBundles\": [{" +
                    "\"destinations\": [{" +
                        "\"name\": \"dest-1\"," +
                        "\"sensitiveData\": \"__sensitive_data__dest-1__sensitive_data__\"" +
                    "}, {" +
                        "\"name\": \"dest-2\"," +
                        "\"sensitiveData\": \"__sensitive_data__dest-2__sensitive_data__\"" +
                    "}]" +
                "}]" +
            "}]" +
        "}";

        String destsFetcherResponse =
        "{" +
            "\"dest-1\": {" +
                "\"password\": \"super-secret\"" +
            "}" +
        "}";

        when(destsFetcherClient.getDestinations(eq(TENANT), eq(X_REQUEST_ID), anyList())).thenReturn((ObjectNode) new ObjectMapper().readTree(destsFetcherResponse));


        TestLogic testLogic = () -> {
            mvc.perform(get(requestPath(Reload.FALSE, ResponseType.JSON)).header(correlationIdHeader, X_REQUEST_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                    containsString("{\"name\": \"dest-1\",\"sensitiveData\": {\"password\":\"super-secret\"}"),
                    containsString("{\"name\": \"dest-2\",\"sensitiveData\": {}")
                ))
            );
        };

        runTest(odataResponse, ResponseType.JSON, testLogic);
    }


    private enum ResponseType { XML, JSON }
    private enum Reload { FALSE, TRUE }

    private String requestPath(Reload reload, ResponseType responseType) {
        String path = "/" + requestMappingPath + "/systemInstances?$expand=consumptionBundles($expand=destinations)";
        if (reload == Reload.TRUE) {
            path += "&reload=true";
        }
        if (responseType == ResponseType.JSON) {
            path += "&$format=json";
        }
        return path;
    }

    interface TestLogic {
        void run() throws Exception;
    }

    private void runTest(String odataResult, ResponseType responseType, TestLogic fn) throws Exception {

        try (MockedConstruction<JPAODataRequestHandler> mocked = mockConstruction(JPAODataRequestHandler.class,
        (mock, context) -> {

            doAnswer(invocation -> {
                HttpServletResponse response = invocation.getArgument(1);

                switch (responseType) {
                    case XML:
                        response.setContentType("application/xml");
                        break;
                    case JSON:
                        response.setContentType("application/json");
                        break;
                    default:
                        break;
                }

                response.getWriter().print(odataResult);

                return null;
            }).when(mock).process(any(HttpServletRequest.class), any(HttpServletResponse.class));
        })) {
            fn.run();
        }
    }
}
