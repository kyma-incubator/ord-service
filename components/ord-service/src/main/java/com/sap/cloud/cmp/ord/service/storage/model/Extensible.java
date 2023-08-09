package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Extensible {
    @Column(name = "extensible_supported")
    private String supported;

    @Column(name = "extensible_description", length = Integer.MAX_VALUE)
    private String description;
}