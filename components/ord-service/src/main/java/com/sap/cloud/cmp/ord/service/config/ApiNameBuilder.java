package com.sap.cloud.cmp.ord.service.config;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import com.sap.olingo.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import com.sap.olingo.jpa.metadata.core.edm.mapper.impl.JPADefaultEdmNameBuilder;

class ApiNameBuilder implements JPAEdmNameBuilder {

    private final JPAEdmNameBuilder defaultNameBuilder;

    ApiNameBuilder(final String punit) {
      defaultNameBuilder = new JPADefaultEdmNameBuilder(punit);
    }

    @Override
    public String buildComplexTypeName(final EmbeddableType<?> jpaEmbeddedType) {
      return defaultNameBuilder.buildComplexTypeName(jpaEmbeddedType);
    }

    @Override
    public String buildContainerName() {
      return defaultNameBuilder.buildContainerName();
    }

    @Override
    public String buildEntitySetName(final String entityTypeName) {
//      return "pkg".equals(entityTypeName) ? defaultNameBuilder.buildEntitySetName("package") : defaultNameBuilder.buildEntitySetName(entityTypeName);
        return "pkg".equals(entityTypeName) ? "package" : defaultNameBuilder.buildEntitySetName(entityTypeName);
    }

    @Override
    public String buildEntityTypeName(final EntityType<?> jpaEntityType) {
      return defaultNameBuilder.buildEntityTypeName(jpaEntityType);
    }

    @Override
    public String buildEnumerationTypeName(final Class<? extends Enum<?>> javaEnum) {
      return defaultNameBuilder.buildEnumerationTypeName(javaEnum);
    }

    @Override
    public String buildNaviPropertyName(final Attribute<?, ?> jpaAttribute) {
      return defaultNameBuilder.buildNaviPropertyName(jpaAttribute);
    }

    @Override
    public String buildOperationName(final String internalOperationName) {
      return defaultNameBuilder.buildOperationName(internalOperationName);
    }

    @Override
    public String buildPropertyName(final String jpaAttributeName) {
      return defaultNameBuilder.buildPropertyName(jpaAttributeName);
    }

    @Override
    public String getNamespace() {
      return defaultNameBuilder.getNamespace();
    }
}