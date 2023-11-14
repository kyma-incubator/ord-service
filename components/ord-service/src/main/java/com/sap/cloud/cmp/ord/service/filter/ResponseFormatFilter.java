package com.sap.cloud.cmp.ord.service.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;

@Component
@Order(Integer.MIN_VALUE)
public class ResponseFormatFilter implements Filter {

    private static final String ACCEPT_HEADER = "accept";

    @Value("${server.default_response_type}")
    private String defaultResponseType;

    private final class FormattedRequest extends HttpServletRequestWrapper {

        public FormattedRequest(ServletRequest request) {
            super((HttpServletRequest) request);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (!name.equals(ACCEPT_HEADER)) {
                return super.getHeaders(name);
            }

            final String query = super.getQueryString();
            final Enumeration<String> headerEnumeration = super.getHeaders(ACCEPT_HEADER);
            final List<String> headerValues = Collections.list(headerEnumeration);

            boolean acceptsEverything = headerValues.size() == 0 || (headerValues.size() == 1 && headerValues.get(0).equals("*/*"));
            boolean formatProvided = query != null && query.contains("$format");

            if (acceptsEverything && !formatProvided) {
                return Collections.enumeration(Arrays.asList("application/" + defaultResponseType));
            }

            return headerEnumeration;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Enumeration<String> headerEnumeration = super.getHeaderNames();
            Set<String> headerNames = new HashSet<>(Collections.list(headerEnumeration));
            headerNames.add(ACCEPT_HEADER);

            return Collections.enumeration(headerNames);
        }
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new FormattedRequest(request), response);
    }

}
