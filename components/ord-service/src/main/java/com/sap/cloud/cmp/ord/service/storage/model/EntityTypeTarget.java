package com.sap.cloud.cmp.ord.service.storage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EntityTypeTarget implements Serializable {
    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;

    @Column(name = "correlation_id", length = Integer.MAX_VALUE)
    private String correlationId;
}
