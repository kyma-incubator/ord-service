package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "changelog_entries_data_products")
@Entity(name = "changelogDataProduct")
@IdClass(ChangelogDataProduct.class)
public class ChangelogDataProduct implements Serializable {
    @Id
    @Column(name = "data_product_id", length = 256)
    private String dataProductId;

    @Id
    @Column(name = "version", length = Integer.MAX_VALUE)
    private String version;

    @Id
    @Column(name = "release_status", length = Integer.MAX_VALUE)
    private String releaseStatus;

    @Id
    @Column(name = "date", length = Integer.MAX_VALUE)
    private String date;

    @Id
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Id
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
