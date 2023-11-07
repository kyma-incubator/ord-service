package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "event_resource_links")
@Entity(name = "eventResourceLink")
@IdClass(EventResourceLinkEntity.class)
public class EventResourceLinkEntity implements Serializable {
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
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
