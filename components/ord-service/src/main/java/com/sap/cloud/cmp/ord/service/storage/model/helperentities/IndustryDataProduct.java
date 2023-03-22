package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "ord_data_products_industry")
@Entity(name = "industryDataProduct")
@IdClass(IndustryDataProduct.class)
public class IndustryDataProduct implements Serializable {
    @javax.persistence.Id
    @Column(name = "data_product_id", length = 256)
    private String dataProductID;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
