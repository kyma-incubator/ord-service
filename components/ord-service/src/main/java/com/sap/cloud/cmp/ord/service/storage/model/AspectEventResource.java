package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import jakarta.persistence.*;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import java.util.List;
import java.util.UUID;

@Entity(name = "aspectEventResource")
@Table(name = "tenants_aspect_event_resources")
public class AspectEventResource {
    // omit @EdmIgnore annotation so that the expanding between `aspects` and `aspectEventResource` can work (in both directions)
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;

    @EdmProtectedBy(name = "formation_scope")
    @EdmIgnore
    @Column(name = "formation_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID formationID;

    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;

    @Column(name = "min_version", length = Integer.MAX_VALUE)
    private String minVersion;

    @Column(name = "aspect_id")
    @EdmIgnore
    private String aspectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "aspect_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private AspectEntity aspect;

    @ElementCollection
    @CollectionTable(name = "aspect_event_resources_subset", joinColumns = @JoinColumn(name = "event_resource_id"))
    private List<AspectEventResourceSubset> subset;
}
