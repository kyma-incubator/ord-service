package com.sap.cloud.cmp.ord.service.common;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class Common {

    public static final String REQUEST_ATTRIBUTE_TENANT_ID = "tenantId";

    public static String buildRequestPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "";
        }

        return request.getServletPath() + pathInfo;
    }

    public static void sendTextResponse(HttpServletResponse response, HttpStatus status, String optionalMessage) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        String responseContent = optionalMessage != null ? optionalMessage : status.getReasonPhrase();
        response.setContentLength(responseContent.length());
        response.getWriter().print(responseContent);
    }

}
