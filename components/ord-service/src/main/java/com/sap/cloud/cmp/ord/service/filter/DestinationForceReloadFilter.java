package com.sap.cloud.cmp.ord.service.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    @Value("${http.headers.correlationId}")
    private String correlationIdHeader;

    @Autowired
    private TokenParser tokenParser;

    @Autowired
    private DestinationFetcherClient destsFetcherClient;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        boolean isODataPath = Common.buildRequestPath(httpRequest).startsWith("/" + odataPath);
        boolean reloadQueryParamIsTrue = Boolean.TRUE.toString().equals(request.getParameter(RELOAD_QUERY_PARAM));

        if (isODataPath && reloadQueryParamIsTrue) {
            Token token = tokenParser.fromRequest(httpRequest);
            String tenant = token == null ? "" : token.extractTenant();
            String correlationId = httpRequest.getHeader(correlationIdHeader);
            if (token == null || tenant == null || tenant.isEmpty()) {
                Common.sendTextResponse((HttpServletResponse) response, HttpStatus.UNAUTHORIZED, null);
                return;
            }

            try {
                destsFetcherClient.reload(tenant, correlationId);
            } catch (RestClientResponseException exc) {
                logger.error("Reload destinations request failed with status: {}, body: {}", exc.getRawStatusCode(), exc.getResponseBodyAsString());
                Common.sendTextResponse((HttpServletResponse) response, HttpStatus.INTERNAL_SERVER_ERROR, null);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
