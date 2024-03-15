package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "event_product_formation")
@Entity(name = "eventProductFormation")
@IdClass(EventProductFormation.class)
public class EventProductFormation implements Serializable {
    @Id
    @Column(name = "event_definition_id", length = 256)
    private String eventDefID;

    @Id
    @Column(name = "product_id", length = 256)
    private String productID;

    @Id
    @Column(name = "formation_id", length = 256)
    private String formationID;
}
