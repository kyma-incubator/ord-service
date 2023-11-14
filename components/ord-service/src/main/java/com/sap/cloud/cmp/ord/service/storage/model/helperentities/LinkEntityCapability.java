package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "links_capabilities")
@Entity(name = "linkEntityCapability")
@IdClass(LinkEntityCapability.class)
public class LinkEntityCapability implements Serializable {
    @Id
    @Column(name = "capability_id", length = 256)
    private String apiDefID;

    @Id
    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Id
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Id
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
