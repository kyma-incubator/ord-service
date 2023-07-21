package com.sap.cloud.cmp.ord.service.storage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ConsumptionBundleReference {
    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @Column(name = "default_entry_point", length = 256)
    private String defaultEntryPoint;

    @Column(name = "default_consumption_bundle")
    private boolean isDefaultBundle;
}
