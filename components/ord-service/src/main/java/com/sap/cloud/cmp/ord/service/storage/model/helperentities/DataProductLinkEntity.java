package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "data_product_links")
@Entity(name = "dataProductLink")
@IdClass(DataProductLinkEntity.class)
public class DataProductLinkEntity implements Serializable {
    @Id
    @Column(name = "data_product_id", length = 256)
    private String dataProductId;

    @Id
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Id
    @Column(name = "custom_type", length = Integer.MAX_VALUE)
    private String customType;

    @Id
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
