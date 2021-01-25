package com.sap.cloud.cmp.ord.service.config;

import javax.servlet.http.HttpServletResponse;

import com.sap.olingo.jpa.processor.core.api.JPAErrorProcessor;
import com.sap.olingo.jpa.processor.core.exception.ODataJPAQueryException;

import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataServerError;

public class CustomErrorProcessor implements JPAErrorProcessor {

    public final static String ADDITIONAL_ERR_MESSAGE = " Use odata-debug query parameter with value one of the following formats: json,html,download for more information.";

    public final String INVALID_TENANT_ID = "Missing or invalid tenantID";

    @Override
    public void processError(ODataRequest oDataRequest, ODataServerError oDataServerError) {
        Exception exc = oDataServerError.getException();
        if (exc != null && exc instanceof ODataJPAQueryException) {
            ODataJPAQueryException jpaException = (ODataJPAQueryException) exc;
            if ("MISSING_CLAIM".equals(jpaException.getId())) {
                // oDataServerError.setCode()
                oDataServerError.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                oDataServerError.setMessage(INVALID_TENANT_ID);
            }
        }
        oDataServerError.setMessage(oDataServerError.getMessage() + ADDITIONAL_ERR_MESSAGE);
    }
}
