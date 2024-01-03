package com.sap.cloud.cmp.ord.service.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.cmp.ord.service.common.Common;
import com.sap.cloud.cmp.ord.service.filter.aggregator.JsonArrayElementsAggregator;
import com.sap.cloud.cmp.ord.service.filter.wrappers.CapturingResponseWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JsonPostProcessFilter implements Filter {

    @Value("${odata.jpa.request_mapping_path}")
    private String odataPath;

    private final static String COMPACT_QUERY_PARAM = "compact";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean isODataPath = Common.buildRequestPath((HttpServletRequest) request).startsWith("/" + odataPath);
        boolean isCompactTrue = Boolean.TRUE.toString().equals(request.getParameter(COMPACT_QUERY_PARAM));

        if (!isODataPath || !isCompactTrue) {
            filterChain.doFilter(request, response);
            return;
        }

        CapturingResponseWrapper capturingResponseWrapper = new CapturingResponseWrapper((HttpServletResponse) response);

        filterChain.doFilter(request, capturingResponseWrapper);

        String content = capturingResponseWrapper.getCaptureAsString();

        if (response.getContentType() != null && response.getContentType().contains("application/json")) {
            // Make JSON returned as String to look like real JSON
            // content = content.replaceAll("\\\\\"","\"").replaceAll("\"\\{","{").replaceAll("}\"", "}");

            // Aggreagate Array Elements
            JsonNode jsonTree = mapper.readTree(content);
            aggregator.aggregate(jsonTree);
            content = mapper.writeValueAsString(jsonTree);
        }

        response.setContentLength(content.length());
        response.getWriter().write(content);
    }
}
