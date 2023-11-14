package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.List;
import java.util.UUID;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "aspectEventResource")
@Table(name = "aspect_event_resources")
public class AspectEventResource {
    // omit @EdmIgnore annotation so that the expanding between `aspects` and `aspectEventResource` can work (in both directions)
    @Id
    @Column(name = "event_resource_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID eventId;

    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;

    @Column(name = "min_version", length = Integer.MAX_VALUE)
    private String minVersion;

    @Column(name = "aspect_id")
    @EdmIgnore
    private String aspectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aspect_id", insertable = false, updatable = false)
    private AspectEntity aspect;

    @ElementCollection
    @CollectionTable(name = "aspect_event_resources_subset", joinColumns = @JoinColumn(name = "event_resource_id"))
    private List<AspectEventResourceSubset> subset;
}
