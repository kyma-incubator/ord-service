package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EdmIgnore
@Table(name = "data_product_successors")
@Entity(name = "data_product_successor")
public class DataProductSuccessor {
    @javax.persistence.Id
    @Column(name = "data_product_id", length = 256)
    private String dataProductId;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
