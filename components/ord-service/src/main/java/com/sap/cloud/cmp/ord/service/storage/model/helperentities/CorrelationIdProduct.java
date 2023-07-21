package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "correlation_ids_products")
@Entity(name = "correlationIdProduct")
@IdClass(CorrelationIdProduct.class)
public class CorrelationIdProduct implements Serializable {
    @jakarta.persistence.Id
    @Column(name = "product_id", length = 256)
    private String productID;

    @jakarta.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
