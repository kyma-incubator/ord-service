package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "links_bundles")
@Entity(name = "linkEntityBundle")
@IdClass(LinkEntityBundle.class)
public class LinkEntityBundle implements Serializable {
    @jakarta.persistence.Id
    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @jakarta.persistence.Id
    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @jakarta.persistence.Id
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @jakarta.persistence.Id
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
