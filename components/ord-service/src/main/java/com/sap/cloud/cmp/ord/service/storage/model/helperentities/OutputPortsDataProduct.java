package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "output_ports_data_products")
@Entity(name = "outputPortsDataProduct")
@IdClass(OutputPortsDataProduct.class)
public class OutputPortsDataProduct implements Serializable {
    @javax.persistence.Id
    @Column(name = "data_product_id", length = 256)
    private String dataProductId;

    @javax.persistence.Id
    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;
}