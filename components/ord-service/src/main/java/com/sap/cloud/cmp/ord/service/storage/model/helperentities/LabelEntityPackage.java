package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "ord_labels_packages")
@Entity(name = "labelPackage")
@IdClass(LabelEntityPackage.class)
public class LabelEntityPackage implements Serializable {
    @jakarta.persistence.Id
    @Column(name = "package_id", length = 256)
    private String packageID;

    @jakarta.persistence.Id
    @Column(name = "key", length = Integer.MAX_VALUE)
    private String key;

    @jakarta.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
