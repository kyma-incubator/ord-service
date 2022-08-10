package com.sap.cloud.cmp.ord.service.filter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sap.cloud.cmp.ord.service.client.DestinationFetcherClient;
import com.sap.cloud.cmp.ord.service.common.Common;
import com.sap.cloud.cmp.ord.service.filter.wrappers.CapturingResponseWrapper;

@Component
public class DestinationSensitiveDataFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(DestinationSensitiveDataFilter.class);

    private static final Pattern sensitiveDataValuePlaceholder = Pattern
            .compile("__sensitive_data__([^\"]+)__sensitive_data__");

    private static String sensitiveDataPlaceholderJSONString(String destinationName) {
        return "\"__sensitive_data__" + destinationName + "__sensitive_data__\"";
    }

    @Value("${odata.jpa.request_mapping_path}")
    private String odataPath;

    @Autowired
    private DestinationFetcherClient destsFetcherClient;

    private final static String DESTINATIONS_ODATA_FILTER = "destinations";

    private String getFullPath(HttpServletRequest servletRequest) {
        String query = servletRequest.getQueryString();
        if (query == null) {
            query = "";
        }
        return Common.buildRequestPath(servletRequest) + query;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String fullPath = getFullPath((HttpServletRequest) request);

        boolean isODataPath = fullPath.startsWith("/" + odataPath);
        boolean hasDestinations = fullPath.contains(DESTINATIONS_ODATA_FILTER);

        if (!isODataPath || !hasDestinations) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletResponse servletResponse = ((HttpServletResponse) response);
        CapturingResponseWrapper capturingResponseWrapper = new CapturingResponseWrapper(servletResponse);

        filterChain.doFilter(request, capturingResponseWrapper);

        String responseContentType = servletResponse.getContentType();
        String responseContent = capturingResponseWrapper.getCaptureAsString();

        if (responseContentType != null) {
            if (responseContentType.contains("application/xml")) {
                String message = "Destination sensitiveData is only available for json responses";
                logger.error(message);
                Common.sendTextResponse((HttpServletResponse) response, HttpStatus.NOT_IMPLEMENTED, message);
                return;
            }

            if (responseContentType.contains("application/json")) {
                String tenantId = (String) request.getAttribute(Common.REQUEST_ATTRIBUTE_TENANT_ID);
                try {
                    responseContent = replaceSensitiveData(tenantId, responseContent);
                } catch (RestClientResponseException exc) {
                    logger.error("Load destinations sensitive data request failed with status: {}, body: {}", exc.getRawStatusCode(), exc.getResponseBodyAsString());
                    Common.sendTextResponse((HttpServletResponse) response, HttpStatus.INTERNAL_SERVER_ERROR, null);
                    return;
                }
            }
        }

        response.setContentLength(responseContent.length());
        response.getWriter().write(responseContent);
    }

    private String replaceSensitiveData(String tenantId, String content) throws JsonProcessingException {
        List<String> destinationNames = getDestinationNames(content);

        ObjectNode sensitiveData = destsFetcherClient.getDestinations(tenantId, destinationNames);
        for (String destinationName : destinationNames) {
            JsonNode destinationSensitiveData = sensitiveData.get(destinationName);

            content = content.replace(
                    sensitiveDataPlaceholderJSONString(destinationName),
                    destinationSensitiveData == null ? "{}" : destinationSensitiveData.toString());
        }

        return content;
    }

    private List<String> getDestinationNames(String content) {
        Matcher matcher = sensitiveDataValuePlaceholder.matcher(content);

        List<String> destinationNames = new LinkedList<>();
        while (matcher.find()) {
            destinationNames.add(matcher.group(1));
        }
        return destinationNames;
    }
}
