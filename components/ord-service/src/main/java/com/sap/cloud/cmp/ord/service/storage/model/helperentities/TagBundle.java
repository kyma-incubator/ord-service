package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "ord_tags_bundles")
@Entity(name = "tagBundle")
@IdClass(TagBundle.class)
public class TagBundle implements Serializable {
    @Id
    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
