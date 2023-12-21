package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "entity_type_targets_entity_type_mappings")
@Entity(name = "entityTypeMappingEntityTypeTarget")
@IdClass(EntityTypeMappingEntityTypeTarget.class)
public class EntityTypeMappingEntityTypeTarget implements Serializable {
    @Id
    @Column(name = "entity_type_mapping_id", length = Integer.MAX_VALUE)
    private String entityTypeMappingID;

    @Id
    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;

    @Id
    @Column(name = "correlation_id", length = Integer.MAX_VALUE)
    private String correlationId;
}