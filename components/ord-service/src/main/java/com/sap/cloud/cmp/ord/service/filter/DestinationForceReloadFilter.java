package com.sap.cloud.cmp.ord.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import com.sap.cloud.cmp.ord.service.client.DestinationFetcherClient;
import com.sap.cloud.cmp.ord.service.common.Common;
import com.sap.cloud.cmp.ord.service.token.Token;
import com.sap.cloud.cmp.ord.service.token.TokenParser;

@Component
public class DestinationForceReloadFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(DestinationForceReloadFilter.class);

    private final static String RELOAD_QUERY_PARAM = "reload";

    @Value("${odata.jpa.request_mapping_path}")
    private String odataPath;

    @Autowired
    private TokenParser tokenParser;

    @Autowired
    private DestinationFetcherClient destsFetcherClient;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean isODataPath = Common.buildRequestPath(httpRequest).startsWith("/" + odataPath);
        boolean reloadQueryParamIsTrue = Boolean.TRUE.toString().equals(request.getParameter(RELOAD_QUERY_PARAM));

        httpResponse.addHeader("x-request-id", httpRequest.getHeader("x-request-id"));
        if (isODataPath && reloadQueryParamIsTrue) {
            Token token = tokenParser.fromRequest(httpRequest);
            String tenant = token == null ? "" : token.extractTenant();
            String xRequestID = httpRequest.getHeader("x-request-id");
            if (token == null || tenant == null || tenant.isEmpty()) {
                Common.sendTextResponse(httpResponse, HttpStatus.UNAUTHORIZED, null);
                return;
            }

            try {
                destsFetcherClient.reload(tenant, xRequestID);
            } catch (RestClientResponseException exc) {
                logger.error("Reload destinations request failed with status: {}, body: {}", exc.getRawStatusCode(), exc.getResponseBodyAsString());
                Common.sendTextResponse(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, null);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
