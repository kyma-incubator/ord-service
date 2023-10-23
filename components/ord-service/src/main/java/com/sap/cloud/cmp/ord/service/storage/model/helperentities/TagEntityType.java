package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "ord_tags_entity_types")
@Entity(name = "tagEntityType")
@IdClass(TagEntityType.class)
public class TagEntityType implements Serializable {
    @javax.persistence.Id
    @Column(name = "entity_type_id", length = 256)
    private String productID;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
