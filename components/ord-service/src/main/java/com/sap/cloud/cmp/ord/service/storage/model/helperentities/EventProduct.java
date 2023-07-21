package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;


@EdmIgnore
@Table(name = "event_product")
@Entity(name = "eventProduct")
@IdClass(EventProduct.class)
public class EventProduct implements Serializable {
    @jakarta.persistence.Id
    @Column(name = "event_definition_id", length = 256)
    private String eventDefID;

    @jakarta.persistence.Id
    @Column(name = "product_id", length = 256)
    private String productID;
}
