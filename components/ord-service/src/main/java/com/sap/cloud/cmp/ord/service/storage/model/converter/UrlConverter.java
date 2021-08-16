package com.sap.cloud.cmp.ord.service.storage.model.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.servlet.http.HttpServletRequest;

@Converter
@Component
public class UrlConverter implements AttributeConverter<String, String> {

    private static Environment env;

    public String convertToDatabaseColumn(String s) {
        return null; // ORD Service is read only
    }

    public String convertToEntityAttribute(String s) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();
        String baseURL = req.getRequestURL().toString().replace(req.getRequestURI(), "");

        return baseURL + "/" + env.getProperty("static.request_mapping_path") + s;
    }

    @Autowired
    public void setEnv(Environment env) {
        UrlConverter.env = env;
    }
}
