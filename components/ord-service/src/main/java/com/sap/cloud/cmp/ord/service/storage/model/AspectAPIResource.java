package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.*;

@Entity(name = "aspectApiResource")
@Table(name = "aspect_api_resources")
public class AspectAPIResource {
    @javax.persistence.Id
    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;

    @Column(name = "min_version", length = Integer.MAX_VALUE)
    private String minVersion;

    @Column(name = "aspect_id")
    @EdmIgnore
    private String aspectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aspect_id", insertable = false, updatable = false)
    private AspectEntity aspect;
}
