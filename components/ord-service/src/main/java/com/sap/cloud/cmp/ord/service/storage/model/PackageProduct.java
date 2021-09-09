package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;


@EdmIgnore
@Table(name = "package_product")
@Entity(name = "packageProduct")
@IdClass(PackageProduct.class)
public class PackageProduct implements Serializable {
    @javax.persistence.Id
    @Column(name = "package_id", length = 256)
    private String packageID;

    @javax.persistence.Id
    @Column(name = "product_id", length = 256)
    private String productID;
}
