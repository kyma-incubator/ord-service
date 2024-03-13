package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

@Entity(name = "entityTypeMapping")
//@Table(name = "tenants_entity_type_mappings")
@Table(name = "tenants_entity_type_mappings_formation")
public class EntityTypeMappingEntity {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "api_definition_id", insertable = false, updatable = false)
//    private APIEntity apiResource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "api_definition_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private APIEntity apiResource;

    @EdmProtectedBy(name = "formation_scope")
    @EdmIgnore
    @Column(name = "formation_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID formationID;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "event_definition_id", insertable = false, updatable = false)
//    private EventEntity eventResource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "event_definition_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private EventEntity eventResource;
}
