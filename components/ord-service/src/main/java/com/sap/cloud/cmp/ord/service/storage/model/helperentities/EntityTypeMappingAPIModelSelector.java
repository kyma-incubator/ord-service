package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "api_model_selectors_entity_type_mappings")
@Entity(name = "entityTypeMappingAPIModelSelector")
@IdClass(EntityTypeMappingAPIModelSelector.class)
public class EntityTypeMappingAPIModelSelector implements Serializable {
    @javax.persistence.Id
    @Column(name = "entity_type_mapping_id", length = Integer.MAX_VALUE)
    private String entityTypeMappingID;

    @javax.persistence.Id
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @javax.persistence.Id
    @Column(name = "entity_set_name", length = Integer.MAX_VALUE)
    private String entitySetName;

    @javax.persistence.Id
    @Column(name = "json_pointer", length = Integer.MAX_VALUE)
    private String jsonPointer;
}