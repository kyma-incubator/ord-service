package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "ord_tags_entity_types")
@Entity(name = "tagEntityType")
@IdClass(TagEntityType.class)
public class TagEntityType implements Serializable {
    @Id
    @Column(name = "entity_type_id", length = 256)
    private String productID;

    @Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
