package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "tags_packages")
@Entity(name = "tagPackage")
@IdClass(TagPackage.class)
public class TagPackage implements Serializable {
    @javax.persistence.Id
    @Column(name = "package_id", length = 256)
    private String packageID;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
