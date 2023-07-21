package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "package_links")
@Entity(name = "packageLink")
@IdClass(PackageLinkEntity.class)
public class PackageLinkEntity implements Serializable {
    @Id
    @Column(name = "package_id", length = 256)
    private String packageID;

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
