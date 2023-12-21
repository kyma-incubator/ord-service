package com.sap.cloud.cmp.ord.service.config;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;

import com.sap.olingo.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import com.sap.olingo.jpa.metadata.core.edm.mapper.impl.JPADefaultEdmNameBuilder;

public class CustomJPAEdmNameBuilder implements JPAEdmNameBuilder {

  private final JPAEdmNameBuilder defaultNameBuilder;

  public CustomJPAEdmNameBuilder(String namespace) {
    defaultNameBuilder = new JPADefaultEdmNameBuilder(namespace);
  }

  @Override
  public String buildComplexTypeName(EmbeddableType<?> embeddable) {
    return defaultNameBuilder.buildComplexTypeName(embeddable);
  }

  @Override
  public String buildContainerName() {
    return defaultNameBuilder.buildContainerName();
  }

  @Override
  public String buildEntitySetName(String setName) {
    return defaultNameBuilder.buildEntitySetName(setName);
  }

  @Override
  public String buildEntityTypeName(EntityType<?> et) {
    return defaultNameBuilder.buildEntityTypeName(et);
  }

  @Override
  public String buildEnumerationTypeName(Class<? extends Enum<?>> enumeration) {
    return defaultNameBuilder.buildEnumerationTypeName(enumeration);
  }

  @Override
  public String buildNaviPropertyName(Attribute<?, ?> jpaAttribute) {
    return firstToLower(jpaAttribute.getName());
  }

  @Override
  public String buildOperationName(String internalOperationName) {
    return defaultNameBuilder.buildOperationName(internalOperationName);
  }

  @Override
  public String buildPropertyName(String internalName) {
    return firstToLower(internalName);
  }

  @Override
  public String getNamespace() {
    return defaultNameBuilder.getNamespace();
  }
  
  public static String firstToLower(String substring) {
    return Character.toLowerCase(substring.charAt(0)) + substring.substring(1);
}

}
