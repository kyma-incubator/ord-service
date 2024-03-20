package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;


@EdmIgnore
@Table(name = "api_product")
@Entity(name = "apiProduct")
@IdClass(APIProduct.class)
public class APIProduct implements Serializable {
    @Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @Id
    @Column(name = "product_id", length = 256)
    private String productID;

    @Id
    @Column(name = "formation_id", length = 256)
    private String formationID;
}
