package com.sap.cloud.cmp.ord.service.storage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AspectEventResourceSubset {
    @Column(name = "event_type", length = Integer.MAX_VALUE, nullable = false)
    private String eventType;
}
