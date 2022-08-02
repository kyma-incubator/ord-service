package com.sap.cloud.cmp.ord.service.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sap.cloud.cmp.ord.service.filter.wrappers.CapturingResponseWrapper;
import com.sap.cloud.cmp.ord.service.filter.wrappers.SensitiveData;

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

    private final static String DESTINATIONS_ODATA_FILTER = "destinations";
    private final static String SENSITIVE_DATA_ODATA_FILTER = "sensitiveData";

    private String getFullPath(HttpServletRequest servletRequest) {
        String query = servletRequest.getQueryString();
        if (query == null) {
            query = "";
        }
        return servletRequest.getServletPath() + query;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        
        String fullPath = getFullPath((HttpServletRequest) request);

        boolean isODataPath = fullPath.startsWith("/" + odataPath);
        boolean hasDestinations = fullPath.contains(DESTINATIONS_ODATA_FILTER);
        boolean hasSensitiveDataSelect = fullPath.contains(SENSITIVE_DATA_ODATA_FILTER);

        if (!isODataPath || !hasDestinations) {
            filterChain.doFilter(request, response);
            return;
        }
        HttpServletResponse servletResponse = ((HttpServletResponse) response);
        CapturingResponseWrapper capturingResponseWrapper = new CapturingResponseWrapper(servletResponse);

        filterChain.doFilter(request, capturingResponseWrapper);
        if (servletResponse.getContentType() == null
                || !servletResponse.getContentType().contains("application/json")) {
            logger.error("Destination sensitiveData is only available for json responses");
            servletResponse.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            return;
        }
        String content = replaceSensitiveData(capturingResponseWrapper.getCaptureAsString(), hasSensitiveDataSelect);

        response.setContentLength(content.length());
        response.getWriter().write(content);
    }

    private String replaceSensitiveData(String content, boolean hasSensitiveDataSelect) {
        List<String> destinationNames = getDestinationNames(content);

        if (!hasSensitiveDataSelect) {
            for (String destinationName : destinationNames) {
                content = content.replace(sensitiveDataPlaceholderJSONString(destinationName), "{}");
            }
            return content;
        }
        Map<String, SensitiveData> sensitiveData = getSensitiveDataForDestinations(destinationNames);
        for (String destinationName : destinationNames) {
            SensitiveData destinationSensitiveData = sensitiveData.get(destinationName);

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

    private Map<String, SensitiveData> getSensitiveDataForDestinations(List<String> destinationNames) {
        // TODO call destination fetcher
        // String query = String.join(',', destinationNames);
        Map<String, SensitiveData> a = new HashMap<>();
        a.put("mydest1", new SensitiveData());
        a.put("mydest2", new SensitiveData());
        return a;
    }
}
