package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "aspect_event_resources_subset")
@Entity(name = "aspectEventResourceSubset")
@IdClass(AspectEventResourceSubset.class)
public class AspectEventResourceSubset implements Serializable {
    @javax.persistence.Id
    @Column(name = "event_resource_id", length = 256)
    private String eventResourceId;

    @javax.persistence.Id
    @Column(name = "event_type", length = Integer.MAX_VALUE)
    private String eventType;
}
