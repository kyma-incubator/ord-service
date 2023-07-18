package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Extensible {
    @Column(name = "supported")
    private String supported;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;
}