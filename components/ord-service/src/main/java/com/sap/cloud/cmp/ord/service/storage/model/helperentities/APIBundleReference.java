package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "api_bundle_reference_formation")
@Entity(name = "apiBundleReference")
public class APIBundleReference {
    @Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @Column(name = "tenant_id", length = 256)
    private String tenantID;

    @Column(name = "formation_id")
    private boolean formationID;
}
