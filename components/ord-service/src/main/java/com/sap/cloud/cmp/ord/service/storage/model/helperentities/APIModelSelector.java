package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class APIModelSelector implements Serializable {
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "entity_set_name", length = Integer.MAX_VALUE)
    private String entitySetName;

    @Column(name = "json_pointer", length = Integer.MAX_VALUE)
    private String jsonPointer;
}
