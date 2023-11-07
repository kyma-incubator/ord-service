package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "entity_type_targets_entity_type_mappings")
@Entity(name = "entityTypeMappingEntityTypeTarget")
@IdClass(EntityTypeMappingEntityTypeTarget.class)
public class EntityTypeMappingEntityTypeTarget implements Serializable {
    @javax.persistence.Id
    @Column(name = "entity_type_mapping_id", length = Integer.MAX_VALUE)
    private String entityTypeMappingID;

    @javax.persistence.Id
    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;

    @javax.persistence.Id
    @Column(name = "correlation_id", length = Integer.MAX_VALUE)
    private String correlationId;
}