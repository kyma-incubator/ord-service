package com.sap.cloud.cmp.ord.service.filter;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@Component
@Order(Integer.MIN_VALUE)
public class ResponseFormatFilter implements Filter {

    private static final String ACCEPT_HEADER = "Accept";

    @Value("${server.default_response_type}")
    private String defaultResponseType;

    private final class FormattedRequest extends HttpServletRequestWrapper {

        public FormattedRequest(ServletRequest request) {
            super((HttpServletRequest) request);
        }

        @Override
        public String getQueryString() {
            String query = super.getQueryString();
            String acceptHeader = super.getHeader(ACCEPT_HEADER);

            boolean acceptsEverything = StringUtils.equals(acceptHeader, "*/*");

            if (!acceptsEverything) {
                return query;
            }

            if (query == null) {
                return "$format=" + defaultResponseType;
            }

            if (!query.contains("$format")) {
                return query + "&$format=" + defaultResponseType;
            }

            return query;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new FormattedRequest(request), response);
    }

}
