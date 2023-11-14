package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity(name = "entityType")
@Table(name = "tenants_entity_types")
public class EntityTypeEntity {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "ord_id", length = 256)
    @NotNull
    private String ordId;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

    @Column(name = "local_tenant_id", length = 256)
    @NotNull
    private String localId;

    @ElementCollection
    @CollectionTable(name = "correlation_ids_entity_types", joinColumns = @JoinColumn(name = "entity_type_id", referencedColumnName = "id"))
    private List<ArrayElement> correlationIds;

    @Column(name = "level", length = 256)
    @NotNull
    private String level;

    @Column(name = "title", length = 256)
    @NotNull
    private String title;

    @Column(name = "short_description", length = 256)
    @NotNull
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "system_instance_aware")
    private boolean systemInstanceAware;

    @ElementCollection
    @CollectionTable(name = "changelog_entries_entity_types", joinColumns = @JoinColumn(name = "entity_type_id"))
    private List<ChangelogEntry> changelogEntries;

    @Column(name = "package_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    @NotNull
    private UUID partOfPackage;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "package_id", insertable = false, updatable = false) 
    private PackageEntity pkg;

    @EdmProtectedBy(name = "visibility_scope")
    @Column(name = "visibility")
    private String visibility;

    @ElementCollection
    @CollectionTable(name = "links_entity_types", joinColumns = @JoinColumn(name = "entity_type_id"))
    private List<Link> links;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "entity_type_product",
            joinColumns = {@JoinColumn(name = "entity_type_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private Set<ProductEntity> products;

    @Column(name = "policy_level", length = 256)
    private String policyLevel;

    @Column(name = "custom_policy_level", length = 256)
    private String customPolicyLevel;

    @Column(name = "release_status")
    @NotNull
    private String releaseStatus;

    @Column(name = "sunset_date")
    private String sunsetDate;

    @ElementCollection
    @CollectionTable(name = "entity_type_successors", joinColumns = @JoinColumn(name = "entity_type_id"))
    private List<ArrayElement> successors;

    @Embedded
    private Extensible extensible;

    @Column(name = "last_update")
    private String lastUpdate;

    @Column(name = "deprecation_date")
    private String deprecationDate;

    @ElementCollection
    @CollectionTable(name = "ord_tags_entity_types", joinColumns = @JoinColumn(name = "entity_type_id", referencedColumnName = "id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "ord_labels_entity_types", joinColumns = @JoinColumn(name = "entity_type_id", referencedColumnName = "id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels_entity_types", joinColumns = @JoinColumn(name = "entity_type_id", referencedColumnName = "id"))
    private List<Label> documentationLabels;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;

    @EdmProtectedBy(name = "formation_scope")
    @EdmIgnore
    @Column(name = "formation_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID formationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private SystemInstanceEntity systemInstance;
}
