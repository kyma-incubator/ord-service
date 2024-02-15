package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "aspect_event_resources_subset")
@Entity(name = "aspectEventResourceSubset")
@IdClass(AspectEventResourceSubset.class)
public class AspectEventResourceSubset implements Serializable {
    @Id
    @Column(name = "event_resource_id", length = 256)
    private String eventResourceId;

    @Id
    @Column(name = "event_type", length = Integer.MAX_VALUE)
    private String eventType;
}