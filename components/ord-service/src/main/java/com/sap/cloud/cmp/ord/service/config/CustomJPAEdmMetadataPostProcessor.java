package com.sap.cloud.cmp.ord.service.config;

import java.util.Collections;

import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;

import com.sap.olingo.jpa.metadata.api.JPAEdmMetadataPostProcessor;
import com.sap.olingo.jpa.metadata.core.edm.mapper.extension.IntermediateEntityContainerAccess;

public class CustomJPAEdmMetadataPostProcessor implements JPAEdmMetadataPostProcessor {

    private String odataApiVersion;

    public CustomJPAEdmMetadataPostProcessor(String odataApiVersion) {
        this.odataApiVersion = odataApiVersion;
    }

    @Override
    public void processEntityContainer(IntermediateEntityContainerAccess container) {
        CsdlAnnotation annotation = new CsdlAnnotation();
        annotation.setQualifier("SchemaVersion");
        annotation.setTerm("com.sap.cloud.cmp.ord.service.SchemaVersion");
        annotation.setExpression(new CsdlConstantExpression(CsdlConstantExpression.ConstantExpressionType.String, this.odataApiVersion));
        container.addAnnotations(Collections.singletonList(annotation));
    }

}
