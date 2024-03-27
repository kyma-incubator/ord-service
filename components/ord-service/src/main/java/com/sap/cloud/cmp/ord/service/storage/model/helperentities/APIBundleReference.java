package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@EdmIgnore
@Table(name = "tenants_api_bundle_reference")
@Entity(name = "apiBundleReference")
public class APIBundleReference implements Serializable {
    @Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @Column(name = "tenant_id", length = 256)
    private String tenantID;

    @Column(name = "formation_id", length = 256)
    private boolean formationID;
}