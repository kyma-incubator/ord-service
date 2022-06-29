package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.cloud.cmp.ord.service.storage.model.converter.UrlConverter;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "api_resource_definitions")
@Entity(name = "apiResourceDefinition")
@IdClass(APIDefinitionEntity.class)
public class APIDefinitionEntity implements Serializable {
    @javax.persistence.Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @javax.persistence.Id
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @javax.persistence.Id
    @Column(name = "custom_type", length = Integer.MAX_VALUE)
    private String customType;

    @javax.persistence.Id
    @Column(name = "media_type", length = Integer.MAX_VALUE)
    private String mediaType;

    @javax.persistence.Id
    @Convert(converter = UrlConverter.class)
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
