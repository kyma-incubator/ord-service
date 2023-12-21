package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.util.UUID;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "bundle_references")
@Entity(name = "bundleReference")
public class BundleReference {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "api_def_id", length = 256)
    private String apiDefID;

    @Column(name = "event_def_id", length = 256)
    private String eventDefID;

    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @Column(name = "api_def_url", length = 256)
    private String defaultEntryPoint;

    @Column(name = "is_default_bundle")
    private boolean isDefaultBundle;
}
