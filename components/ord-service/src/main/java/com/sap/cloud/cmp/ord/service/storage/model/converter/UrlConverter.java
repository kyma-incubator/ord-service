package com.sap.cloud.cmp.ord.service.storage.model.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.servlet.http.HttpServletRequest;

@Converter
@Component
public class UrlConverter implements AttributeConverter<String, String> {

    private static final String EXTERNAL_HOST_HEADER = "x-external-host";
    private static final String PROTOCOL_HEADER = "x-forwarded-proto";

    private static Environment env;

    public String convertToDatabaseColumn(String s) {
        return null; // ORD Service is read only
    }

    public String convertToEntityAttribute(String s) {
        String protocol = env.getProperty("specification.protocol");

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();
        String baseURL = req.getRequestURL().toString().replace(req.getRequestURI(), "");

        String externalHost = req.getHeader(EXTERNAL_HOST_HEADER);
        if (externalHost != null && !externalHost.isEmpty()) { // ORD Service is behind a reverse proxy (istio ingressgateway when running in cluster)
            baseURL = protocol + "://" + externalHost;
        }

        return baseURL + "/" + env.getProperty("static.request_mapping_path") + s;
    }

    @Autowired
    public void setEnv(Environment env) {
        UrlConverter.env = env;
    }
}
