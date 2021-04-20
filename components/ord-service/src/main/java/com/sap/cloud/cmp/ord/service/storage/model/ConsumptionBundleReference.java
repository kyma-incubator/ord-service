package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ConsumptionBundleReference {
    @Column(name = "bundle_id", length = 256)
    private String ordId;

    @Column(name = "default_entry_point", length = 256)
    private String defaultEntryPoint;
}
