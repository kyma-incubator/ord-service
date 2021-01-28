package com.sap.cloud.cmp.ord.service.config;

import com.sap.olingo.jpa.metadata.api.JPAEdmMetadataPostProcessor;
import com.sap.olingo.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import com.sap.olingo.jpa.metadata.core.edm.mapper.extention.*;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlExpression;

import java.util.Arrays;
import java.util.Collections;

public class CustomJPAEdmMetadataPostProcessor extends JPAEdmMetadataPostProcessor {

    private String odataApiVersion;

    public CustomJPAEdmMetadataPostProcessor(String odataApiVersion) {
        this.odataApiVersion = odataApiVersion;
    }

    public static String firstToLower(String substring) {
        return Character.toLowerCase(substring.charAt(0)) + substring.substring(1);
    }

    @Override
    public void processEntityContainer(IntermediateEntityContainerAccess container) {
        CsdlAnnotation annotation = new CsdlAnnotation();
        annotation.setQualifier("SchemaVersion");
        annotation.setTerm("com.sap.cloud.cmp.ord.service.SchemaVersion");
        annotation.setExpression(new CsdlConstantExpression(CsdlConstantExpression.ConstantExpressionType.String, this.odataApiVersion));
        container.addAnnotations(Collections.singletonList(annotation));
        super.processEntityContainer(container);
    }

    @Override
    public void processEntityType(IntermediateEntityTypeAccess property) {
        // EMPTY BODY
    }

    @Override
    public void processNavigationProperty(IntermediateNavigationPropertyAccess property, String s) {
        property.setExternalName(firstToLower(property.getInternalName()));
    }

    @Override
    public void processProperty(IntermediatePropertyAccess property, String jpaManagedTypeClassName) {
        property.setExternalName(firstToLower(property.getInternalName()));
    }

    @Override
    public void provideReferences(IntermediateReferenceList property) throws ODataJPAModelException {
        // EMPTY BODY
    }
}
