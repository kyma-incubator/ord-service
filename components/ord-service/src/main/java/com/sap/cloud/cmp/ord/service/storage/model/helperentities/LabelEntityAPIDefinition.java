package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "ord_labels_api_definitions")
@Entity(name = "labelAPIDefinition")
@IdClass(LabelEntityAPIDefinition.class)
public class LabelEntityAPIDefinition implements Serializable {
    @jakarta.persistence.Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @jakarta.persistence.Id
    @Column(name = "key", length = Integer.MAX_VALUE)
    private String key;

    @jakarta.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
