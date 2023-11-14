package com.sap.cloud.cmp.ord.service.config;

import javax.servlet.http.HttpServletResponse;

import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataServerError;

import com.sap.olingo.jpa.processor.core.api.JPAErrorProcessor;
import com.sap.olingo.jpa.processor.core.exception.ODataJPAQueryException;

public class CustomErrorProcessor implements JPAErrorProcessor {

    public static final String ADDITIONAL_ERR_MESSAGE = " Use odata-debug query parameter with value one of the following formats: json,html,download for more information.";

    public static final String INVALID_TENANT_ID_ERROR_MESSAGE = "Missing or invalid tenantID.";
    public static final String INVALID_TENANT_ID_ERROR_CODE = "INVALID_TENANT_ID";
    public static final String JPA_EXCEPTION_ERROR_ID = "MISSING_CLAIM";


    @Override
    public void processError(ODataRequest oDataRequest, ODataServerError oDataServerError) {
        Exception exc = oDataServerError.getException();

        // Note: this is needed to cover the case in which the tenant_id is missing from the header or is invalid UUID.
        // This also allows to return the error response body in the appropriate format.
        if (exc instanceof ODataJPAQueryException) {
            ODataJPAQueryException jpaException = (ODataJPAQueryException) exc;

            if (JPA_EXCEPTION_ERROR_ID.equals(jpaException.getId())) {
                oDataServerError.setCode(INVALID_TENANT_ID_ERROR_CODE);
                oDataServerError.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                oDataServerError.setMessage(INVALID_TENANT_ID_ERROR_MESSAGE);
            }
        }
        oDataServerError.setMessage(oDataServerError.getMessage() + ADDITIONAL_ERR_MESSAGE);
    }
}
