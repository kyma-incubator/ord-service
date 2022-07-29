package com.sap.cloud.cmp.ord.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DestinationSensitiveDataFilter implements Filter {

    @Value("${odata.jpa.request_mapping_path}")
    private String odataPath;

    private final static String SENSITIVE_DATA_ODATA_FILTER = "sensitiveData";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        boolean isODataPath = servletRequest.getServletPath().startsWith("/" + odataPath);
        String query = servletRequest.getQueryString();
        boolean hasSensitiveDataFilter = query != null && query.contains(SENSITIVE_DATA_ODATA_FILTER);
        
        filterChain.doFilter(request, response);
        if (isODataPath && hasSensitiveDataFilter) {
            injectSensitiveData();
        }
    }

    private void injectSensitiveData() {
        System.out.println(">>> must inject body");
    }
}
