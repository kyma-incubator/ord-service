package com.sap.cloud.cmp.ord.service.storage.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class APIModelSelector implements Serializable {
    private static final long serialVersionUID = -8149719418262015740L;

    @Column(name = "type", length = Integer.MAX_VALUE, nullable = false)
    private String type;

    @Column(name = "entity_set_name", length = Integer.MAX_VALUE)
    private String entitySetName;

    @Column(name = "json_pointer", length = Integer.MAX_VALUE)
    private String jsonPointer;
}
