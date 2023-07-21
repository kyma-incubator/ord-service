package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.cloud.cmp.ord.service.storage.model.converter.UrlConverter;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "api_resource_definitions")
@Entity(name = "apiResourceDefinition")
@IdClass(APIDefinitionEntity.class)
public class APIDefinitionEntity implements Serializable {
    @jakarta.persistence.Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @jakarta.persistence.Id
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @jakarta.persistence.Id
    @Column(name = "custom_type", length = Integer.MAX_VALUE)
    private String customType;

    @jakarta.persistence.Id
    @Column(name = "media_type", length = Integer.MAX_VALUE)
    private String mediaType;

    @jakarta.persistence.Id
    @Convert(converter = UrlConverter.class)
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
