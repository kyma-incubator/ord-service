package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ExtensibleEntity {
    @Column(name = "supported")
    private String supported;

    @Column(name = "description")
    private String description;
}