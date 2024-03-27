package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinColumn;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import java.util.UUID;

@Entity(name = "aspectApiResource")
@Table(name = "aspect_api_resources")
public class AspectAPIResource {
    @Id
    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;

    @Column(name = "min_version", length = Integer.MAX_VALUE)
    private String minVersion;

    @Column(name = "aspect_id")
    @EdmIgnore
    private String aspectId;

    @EdmProtectedBy(name = "formation_scope")
    @EdmIgnore
    @Column(name = "formation_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID formationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "aspect_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private AspectEntity aspect;
}
