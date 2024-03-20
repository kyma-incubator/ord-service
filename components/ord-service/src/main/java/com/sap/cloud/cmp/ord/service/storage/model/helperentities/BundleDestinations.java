package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;
import java.util.UUID;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "tenants_destinations")
@Entity(name = "bundleDestinations")
public class BundleDestinations implements Serializable {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "tenant_id", length = 256)
    private String tenantID;

    @Column(name = "formation_id", length = 256)
    private String formationID;

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "type", length = 256)
    private String type;

    @Column(name = "url")
    private String url;

    @Column(name = "authentication")
    private String authentication;

    @Column(name = "bundle_id")
    private String bundleID;

    @Column(name = "sensitive_data")
    private String sensitiveData;
}