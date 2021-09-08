package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EdmIgnore
@Table(name = "bundle_references")
@Entity(name = "bundleReference")
public class BundleReference {
    @Column(name = "api_def_id", length = 256)
    private String apiDefID;

    @Column(name = "event_def_id", length = 256)
    private String eventDefID;

    @javax.persistence.Id
    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @Column(name = "api_def_url", length = 256)
    private String defaultEntryPoint;
}
