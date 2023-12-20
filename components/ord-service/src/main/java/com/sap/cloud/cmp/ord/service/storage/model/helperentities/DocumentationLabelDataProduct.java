package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "ord_documentation_labels_data_products")
@Entity(name = "documentationLabelDataProduct")
@IdClass(DocumentationLabelDataProduct.class)
public class DocumentationLabelDataProduct implements Serializable {
    @javax.persistence.Id
    @Column(name = "data_product_id", length = 256)
    private String dataProductId;

    @javax.persistence.Id
    @Column(name = "key", length = Integer.MAX_VALUE)
    private String key;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
