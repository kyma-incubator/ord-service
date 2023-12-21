package com.sap.cloud.cmp.ord.service.storage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Label {
    @Column(name = "key", length = Integer.MAX_VALUE)
    private String key;

    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
