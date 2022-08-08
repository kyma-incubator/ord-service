package com.sap.cloud.cmp.ord.service.destinations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientResponseException;

import com.sap.cloud.cmp.ord.service.client.DestinationFetcherClient;
import com.sap.cloud.cmp.ord.service.controller.ODataController;
import com.sap.cloud.cmp.ord.service.filter.DestinationForceReloadFilter;
import com.sap.cloud.cmp.ord.service.filter.DestinationSensitiveDataFilter;
import com.sap.cloud.cmp.ord.service.token.Token;
import com.sap.cloud.cmp.ord.service.token.TokenParser;
import com.sap.olingo.jpa.processor.core.api.JPAODataCRUDContextAccess;
import com.sap.olingo.jpa.processor.core.api.JPAODataGetHandler;
import com.sap.olingo.jpa.processor.core.processor.JPAODataRequestContextImpl;


@RunWith(SpringRunner.class)
public class FetchingDestinationsTest {

    private static final String TOKEN_VALUE = "eyAiYWxnIjogIlJTMjU2IiwgImtpZCI6ICI4NTQ3NzFlMi00MGFlLTQyMzctOTcwNi1kMTg5NDc2M2Y4N2IiLCAidHlwIjogIkpXVCIgfQo.eyAiY29uc3VtZXJzIjogIlt7XCJDb25zdW1lcklEXCI6XCJhZG1pblwiLFwiQ29uc3VtZXJUeXBlXCI6XCJTdGF0aWMgVXNlclwiLFwiRmxvd1wiOlwiSldUXCJ9XSIsICJleHAiOiAxNjMzMTAxNTI5LCAiaWF0IjogMTYzMzA5NzkyOSwgImlzcyI6ICJodHRwczovL29hdGhrZWVwZXIua3ltYS5sb2NhbC8iLCAianRpIjogIjg1NmNkNWU4LWQ4NzEtNDM5MS04MDI5LTI4NmRjZTcyMzRjZiIsICJuYmYiOiAxNjMzMDk3OTI5LCAic2NvcGVzIjogImFwcGxpY2F0aW9uOnJlYWQgYXBwbGljYXRpb246d3JpdGUgYXBwbGljYXRpb25fdGVtcGxhdGU6cmVhZCBhcHBsaWNhdGlvbl90ZW1wbGF0ZTp3cml0ZSBpbnRlZ3JhdGlvbl9zeXN0ZW06cmVhZCBpbnRlZ3JhdGlvbl9zeXN0ZW06d3JpdGUgcnVudGltZTpyZWFkIHJ1bnRpbWU6d3JpdGUgbGFiZWxfZGVmaW5pdGlvbjpyZWFkIGxhYmVsX2RlZmluaXRpb246d3JpdGUgZXZlbnRpbmc6bWFuYWdlIHRlbmFudDpyZWFkIGF1dG9tYXRpY19zY2VuYXJpb19hc3NpZ25tZW50OnJlYWQgYXV0b21hdGljX3NjZW5hcmlvX2Fzc2lnbm1lbnQ6d3JpdGUgYXBwbGljYXRpb24uYXV0aHM6cmVhZCBhcHBsaWNhdGlvbi53ZWJob29rczpyZWFkIGFwcGxpY2F0aW9uX3RlbXBsYXRlLndlYmhvb2tzOnJlYWQgYnVuZGxlLmluc3RhbmNlX2F1dGhzOnJlYWQgZG9jdW1lbnQuZmV0Y2hfcmVxdWVzdDpyZWFkIGV2ZW50X3NwZWMuZmV0Y2hfcmVxdWVzdDpyZWFkIGFwaV9zcGVjLmZldGNoX3JlcXVlc3Q6cmVhZCBpbnRlZ3JhdGlvbl9zeXN0ZW0uYXV0aHM6cmVhZCBydW50aW1lLmF1dGhzOnJlYWQgZmV0Y2gtcmVxdWVzdC5hdXRoOnJlYWQgd2ViaG9va3MuYXV0aDpyZWFkIGludGVybmFsX3Zpc2liaWxpdHk6cmVhZCIsICJzdWIiOiAiQ2lCcmQzbDNielUxY1hZek1UZGhNWGsxYjNKbGNUZGpaM1pxY3pNMk1XMXRieElGYkc5allXdyIsICJ0ZW5hbnQiOiJ7XCJjb25zdW1lclRlbmFudFwiOlwiM2U2NGViYWUtMzhiNS00NmEwLWIxZWQtOWNjZWUxNTNhMGFlXCIsXCJleHRlcm5hbFRlbmFudFwiOlwiM2U2NGViYWUtMzhiNS00NmEwLWIxZWQtOWNjZWUxNTNhMGFlXCJ9IiB9Cg.";
    private static final String TENANT = "3e64ebae-38b5-46a0-b1ed-9ccee153a0ae";

    private MockMvc mvc;

    @Mock
    private TokenParser tokenParser;

    @Mock
    private JPAODataCRUDContextAccess serviceContext;

    @Mock
    private DestinationFetcherClient destsFetcherClient;

    @InjectMocks
    private ODataController odataController;

    @InjectMocks
    private DestinationForceReloadFilter reloadFilter;

    @InjectMocks
    private DestinationSensitiveDataFilter sensitiveDataFilter;

    @org.junit.Before
    public void setup() {
        String odataPath = "open-resource-discovery-service/v0";
        ReflectionTestUtils.setField(reloadFilter, "odataPath", odataPath);
        ReflectionTestUtils.setField(sensitiveDataFilter, "odataPath", odataPath);

        mvc = MockMvcBuilders.standaloneSetup(odataController)
                .addPlaceholderValue("odata.jpa.request_mapping_path", odataPath)
                .addFilters(reloadFilter)
                .addFilters(sensitiveDataFilter)
                .build();
    }

    @org.junit.After
    public void cleanup() {
        reset(tokenParser);
        reset(destsFetcherClient);
    }

    @Test
    public void testReloadFilter_ReturnsUnauthorized_WhenTokenIsNotProvided() throws Exception {
        when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(null);

        TestLogic testLogic = () -> {
            mvc.perform(
                get(requestPath(Reload.TRUE, SensitiveData.FALSE, ResponseType.JSON)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(HttpStatus.UNAUTHORIZED.getReasonPhrase()));
        };

        runTest(null, testLogic);
    }

    @Test
    public void testReloadFilter_ReturnsInternalServerError_WhenCallToDestinationFetcherFails() throws Exception {
        when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(new Token(null, TOKEN_VALUE));
        doThrow(new RestClientResponseException("Request failed",
            HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
            null, null, null)
        ).when(destsFetcherClient).reload(TENANT);

        TestLogic testLogic = () -> {
            mvc.perform(
                get(requestPath(Reload.TRUE, SensitiveData.FALSE, ResponseType.JSON)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        };

        runTest(null, testLogic);
    }

    @Test
    public void testReloadFilter_ShouldNotCallDestinationsFetcher_WhenReloadQueryIsNotProvided() throws Exception {
        when(tokenParser.fromRequest(any(HttpServletRequest.class))).thenReturn(new Token(null, TOKEN_VALUE));
        TestLogic testLogic = () -> {
            mvc.perform(
                get(requestPath(Reload.FALSE, SensitiveData.FALSE, ResponseType.JSON)))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"));

            verify(destsFetcherClient, never()).reload(anyString());
        };

        runTest("{}", testLogic);
    }


    private enum ResponseType { XML, JSON }
    private enum Reload { FALSE, TRUE }
    private enum SensitiveData { FALSE, TRUE }

    private String requestPath(Reload reload, SensitiveData sensitiveData, ResponseType responseType) {
        String path = "/open-resource-discovery-service/v0/systemInstances?$expand=consumptionBundles($expand=destinations)";
        if (reload == Reload.TRUE) {
            path += "&reload=true";
        }
        if (sensitiveData == SensitiveData.TRUE) {
            path += "&sensitiveData";
        }
        if (responseType == ResponseType.JSON) {
            path += "&$format=json";
        }
        return path;
    }

    interface TestLogic {
        void run() throws Exception;
    }

    private void runTest(String odataResult, TestLogic fn) throws Exception {
        try (MockedConstruction<JPAODataGetHandler> mocked = mockConstruction(JPAODataGetHandler.class,
        (mock, context) -> {
            when(mock.getJPAODataRequestContext()).thenReturn(new JPAODataRequestContextImpl());

            doAnswer(invocation -> {
                HttpServletResponse response = invocation.getArgument(1);
                response.getWriter().print(odataResult);
                return null;
            }).when(mock).process(any(HttpServletRequest.class), any(HttpServletResponse.class));
        })) {
            fn.run();
        }
    }
}
