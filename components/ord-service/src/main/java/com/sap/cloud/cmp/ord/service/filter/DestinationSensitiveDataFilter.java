package com.sap.cloud.cmp.ord.service.filter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sap.cloud.cmp.ord.service.client.DestinationFetcherClient;
import com.sap.cloud.cmp.ord.service.common.Common;
import com.sap.cloud.cmp.ord.service.filter.wrappers.CapturingResponseWrapper;

@Component
public class DestinationSensitiveDataFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(DestinationSensitiveDataFilter.class);

    // OData response
    private static final Pattern sensitiveDataValuePlaceholder = Pattern
            .compile("__sensitive_data__([^\"<>]+)__sensitive_data__");

    // XML
    private static final String DESTINATION_SENSITIVE_DATA_XML_ROOT = "__sensitive_data__";

    private static final Pattern XML_ROOT_ELEMENT_START_TAG_PATTERN = Pattern
            .compile("<" + "\\s*" + DESTINATION_SENSITIVE_DATA_XML_ROOT + "\\s*" + ">");
    private static final Pattern XML_ROOT_ELEMENT_END_TAG_PATTERN = Pattern
            .compile("<" + "\\s*" + "/" + "\\s*" + DESTINATION_SENSITIVE_DATA_XML_ROOT + "\\s*" + ">");

    private static String sensitiveDataPlaceholderXML(String destinationName) {
        return "__sensitive_data__" + destinationName + "__sensitive_data__";
    }

    // JSON
    private static String sensitiveDataPlaceholderJSONString(String destinationName) {
        return "\"__sensitive_data__" + destinationName + "__sensitive_data__\"";
    }

    @Value("${odata.jpa.request_mapping_path}")
    private String odataPath;
    @Value("${http.headers.correlationId}")
    private String correlationIdHeader;

    @Autowired
    private DestinationFetcherClient destsFetcherClient;

    private final static String DESTINATIONS_ODATA_FILTER = "destinations";

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
            String tenantId = (String) request.getAttribute(Common.REQUEST_ATTRIBUTE_TENANT_ID);
            List<String> destinationNames = getDestinationNames(responseContent);
            String correlationId = ((HttpServletRequest) request).getHeader(correlationIdHeader);

            try {
                ObjectNode sensitiveData = loadSensitiveData(tenantId, correlationId, destinationNames);

                if (responseContentType.contains("application/xml")) {
                    responseContent = replaceSensitiveDataXML(destinationNames, sensitiveData, responseContent);
                }

                if (responseContentType.contains("application/json")) {
                    responseContent = replaceSensitiveDataJSON(destinationNames, sensitiveData, responseContent);
                }
            } catch (RestClientResponseException exc) {
                logger.error("Load destinations sensitive data request failed with status: {}, body: {}", exc.getRawStatusCode(), exc.getResponseBodyAsString());
                Common.sendTextResponse((HttpServletResponse) response, HttpStatus.INTERNAL_SERVER_ERROR, null);
                return;
            }

        }

        response.setContentLength(responseContent.length());
        response.getWriter().write(responseContent);
    }

    private String getFullPath(HttpServletRequest servletRequest) {
        String query = servletRequest.getQueryString();
        if (query == null) {
            query = "";
        }
        return Common.buildRequestPath(servletRequest) + query;
    }

    private ObjectNode loadSensitiveData(String tenantId, String correlationId, List<String> destinationNames) throws IOException {
        if (destinationNames.size() == 0) {
            return null;
        }

        return destsFetcherClient.getDestinations(tenantId, correlationId, destinationNames);
    }

    private String replaceSensitiveDataXML(List<String> destinationNames, ObjectNode sensitiveData, String content) throws IOException {
        for (String destinationName : destinationNames) {
            JsonNode destinationSensitiveData = sensitiveData.get(destinationName);

            if (destinationSensitiveData == null) {
                content = content.replace(sensitiveDataPlaceholderXML(destinationName), "");
                continue;
            }

            XmlMapper mapper = new XmlMapper();
            String destinationXML = mapper.writer()
                .withRootName(DESTINATION_SENSITIVE_DATA_XML_ROOT)
                .writeValueAsString(destinationSensitiveData);

            destinationXML = removeRootElementXML(destinationXML);
            content = content.replace(sensitiveDataPlaceholderXML(destinationName), destinationXML);
        }

        return content;
    }

    private String removeRootElementXML(String xml) {
        xml = XML_ROOT_ELEMENT_START_TAG_PATTERN.matcher(xml).replaceFirst("");
        xml = XML_ROOT_ELEMENT_END_TAG_PATTERN.matcher(xml).replaceFirst("");
        return xml;
    }

    private String replaceSensitiveDataJSON(List<String> destinationNames, ObjectNode sensitiveData, String content) throws IOException {
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
