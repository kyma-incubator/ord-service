package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "correlation_ids_bundles")
@Entity(name = "correlationIdBundle")
@IdClass(CorrelationIdBundle.class)
public class CorrelationIdBundle implements Serializable {
    @javax.persistence.Id
    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
