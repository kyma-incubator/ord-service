package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "event_api_definition_extensible")
@Entity(name = "eventExtensible")
@IdClass(EventExtensible.class)
public class EventExtensible implements Serializable {
    @Id
    @Column(name = "event_definition_id", length = 256)
    private String eventDefID;

    @Id
    @Column(name = "supported")
    private String supported;

    @Id
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;
}
