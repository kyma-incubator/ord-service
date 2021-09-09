package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;


@EdmIgnore
@Table(name = "api_product")
@Entity(name = "apiProduct")
@IdClass(APIProduct.class)
public class APIProduct implements Serializable {
    @javax.persistence.Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @javax.persistence.Id
    @Column(name = "product_id", length = 256)
    private String productID;
}
