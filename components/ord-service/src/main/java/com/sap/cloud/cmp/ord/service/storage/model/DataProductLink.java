package com.sap.cloud.cmp.ord.service.storage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DataProductLink {
    @Column(name = "type", length = Integer.MAX_VALUE, nullable = false)
    private String type;

    @Column(name = "custom_type", length = Integer.MAX_VALUE)
    private String customType;

    @Column(name = "url", length = Integer.MAX_VALUE, nullable = false)
    private String url;
}
