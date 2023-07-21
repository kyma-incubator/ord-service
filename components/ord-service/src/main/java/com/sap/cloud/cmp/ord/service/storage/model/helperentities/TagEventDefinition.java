package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "tags_event_definitions")
@Entity(name = "tagEventDefinition")
@IdClass(TagEventDefinition.class)
public class TagEventDefinition implements Serializable {
    @jakarta.persistence.Id
    @Column(name = "event_definition_id", length = 256)
    private String eventDefID;

    @jakarta.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
