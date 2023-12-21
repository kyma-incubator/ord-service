package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.cloud.cmp.ord.service.storage.model.converter.UrlConverter;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "api_resource_definitions")
@Entity(name = "apiResourceDefinition")
@IdClass(APIDefinitionEntity.class)
public class APIDefinitionEntity implements Serializable {
    @Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @Id
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Id
    @Column(name = "custom_type", length = Integer.MAX_VALUE)
    private String customType;

    @Id
    @Column(name = "media_type", length = Integer.MAX_VALUE)
    private String mediaType;

    @Id
    @Convert(converter = UrlConverter.class)
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
