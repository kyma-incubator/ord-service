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
public class DestinationForceResyncFilter implements Filter {

    @Value("${odata.jpa.request_mapping_path}")
    private String oDataPath;

    private final static String RELOAD_QUERY_PARAM = "reload";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean isODataPath = ((HttpServletRequest) request).getServletPath().startsWith("/" + oDataPath);
        boolean reloadQueryParamIsTrue = Boolean.TRUE.toString().equals(request.getParameter(RELOAD_QUERY_PARAM));

        if (isODataPath && reloadQueryParamIsTrue) {
            callDestinationFetcher();
        }
        filterChain.doFilter(request, response);
    }

    private void callDestinationFetcher() {
        System.out.println(">>> callDestFetcher");
    }
}
