package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "input_ports_data_products")
@Entity(name = "inputPortsDataProduct")
@IdClass(InputPortsDataProduct.class)
public class InputPortsDataProduct implements Serializable {
    @Id
    @Column(name = "data_product_id", length = 256)
    private String dataProductId;

    @Id
    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;
}
