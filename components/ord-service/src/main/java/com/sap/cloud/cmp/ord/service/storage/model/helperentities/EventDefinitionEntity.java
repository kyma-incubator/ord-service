package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.cloud.cmp.ord.service.storage.model.converter.UrlConverter;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "event_resource_definitions")
@Entity(name = "eventResourceDefinition")
@IdClass(EventDefinitionEntity.class)
public class EventDefinitionEntity implements Serializable {
    @Id
    @Column(name = "event_definition_id", length = 256)
    private String eventDefID;

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
