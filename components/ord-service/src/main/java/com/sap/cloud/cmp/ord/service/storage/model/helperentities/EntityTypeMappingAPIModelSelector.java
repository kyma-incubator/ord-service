package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "api_model_selectors_entity_type_mappings")
@Entity(name = "entityTypeMappingAPIModelSelector")
@IdClass(EntityTypeMappingAPIModelSelector.class)
public class EntityTypeMappingAPIModelSelector implements Serializable {
    @Id
    @Column(name = "entity_type_mapping_id", length = Integer.MAX_VALUE)
    private String entityTypeMappingID;

    @Id
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Id
    @Column(name = "entity_set_name", length = Integer.MAX_VALUE)
    private String entitySetName;

    @Id
    @Column(name = "json_pointer", length = Integer.MAX_VALUE)
    private String jsonPointer;
}