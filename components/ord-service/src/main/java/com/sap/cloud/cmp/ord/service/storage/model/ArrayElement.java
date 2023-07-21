package com.sap.cloud.cmp.ord.service.storage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ArrayElement {

    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
