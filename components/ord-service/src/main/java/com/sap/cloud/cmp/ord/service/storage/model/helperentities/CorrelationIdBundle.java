package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "correlation_ids_bundles")
@Entity(name = "correlationIdBundle")
@IdClass(CorrelationIdBundle.class)
public class CorrelationIdBundle implements Serializable {
    @jakarta.persistence.Id
    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @jakarta.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
