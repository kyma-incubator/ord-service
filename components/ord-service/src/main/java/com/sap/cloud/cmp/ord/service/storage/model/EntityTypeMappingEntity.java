package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.cloud.cmp.ord.service.storage.model.helperentities.APIModelSelector;
import com.sap.cloud.cmp.ord.service.storage.model.helperentities.EntityTypeTarget;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

@Entity(name = "entityTypeMapping")
@Table(name = "tenants_entity_type_mappings")
public class EntityTypeMappingEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @EdmIgnore
    @Column(name = "api_definition_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID apiResourceID;

    @EdmIgnore
    @Column(name = "event_definition_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID eventResourceID;

    @ElementCollection
    @CollectionTable(name = "api_model_selectors_entity_type_mappings", joinColumns = @JoinColumn(name = "entity_type_mapping_id", referencedColumnName = "id"))
    private List<APIModelSelector> apiModelSelectors;

    @ElementCollection
    @CollectionTable(name = "entity_type_targets_entity_type_mappings", joinColumns = @JoinColumn(name = "entity_type_mapping_id", referencedColumnName = "id"))
    private List<EntityTypeTarget> entityTypeTargets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_definition_id", insertable = false, updatable = false)
    private APIEntity apiResource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_definition_id", insertable = false, updatable = false)
    private EventEntity eventResource;
}
