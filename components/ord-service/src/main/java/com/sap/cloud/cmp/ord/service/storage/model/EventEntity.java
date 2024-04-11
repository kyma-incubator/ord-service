package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Embedded;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.validation.constraints.NotNull;

@Entity(name = "event")
@Table(name = "tenants_events")
public class EventEntity {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "ord_id", length = 256, nullable = false)
    private String ordId;

    @Column(name = "name", length = 256, nullable = false)
    private String title;

    @Column(name = "short_description", length = 256, nullable = false)
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE, nullable = false)
    private String description;

    @EdmProtectedBy(name = "visibility_scope")
    @Column(name = "visibility", nullable = false)
    private String visibility;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "version_value", nullable = false)
    private String version;

    @Column(name = "system_instance_aware")
    private boolean systemInstanceAware;

    @Column(name = "policy_level", length = 256)
    private String policyLevel;

    @Column(name = "custom_policy_level", length = 256)
    private String customPolicyLevel;

    @ElementCollection
    @CollectionTable(name = "correlation_ids_events", joinColumns = @JoinColumn(name = "event_id", referencedColumnName= "id"))
    private List<ArrayElement> correlationIds;

    @Column(name = "local_tenant_id", length = 256)
    private String localId;

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

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "app_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private SystemInstanceEntity systemInstance;

    @OneToMany(mappedBy = "eventResource", fetch = FetchType.LAZY)
    private Set<EntityTypeMappingEntity> entityTypeMappings;

    @ElementCollection
    @CollectionTable(name = "changelog_entries_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ChangelogEntry> changelogEntries;

    @Column(name = "package_id", nullable = false)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID partOfPackage;

    @ElementCollection
    @CollectionTable(name = "event_bundle_reference", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ConsumptionBundleReference> partOfConsumptionBundles;

    @ElementCollection
    @CollectionTable(name = "links_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<Link> links;

    @ElementCollection
    @CollectionTable(name = "tags_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "countries_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ArrayElement> countries;

    @ElementCollection
    @CollectionTable(name = "line_of_businesses_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ArrayElement> lineOfBusiness;

    @ElementCollection
    @CollectionTable(name = "industries_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ArrayElement> industry;

    @Column(name = "release_status", nullable = false)
    private String releaseStatus;

    @Column(name = "sunset_date")
    private String sunsetDate;

    @ElementCollection
    @CollectionTable(name = "event_api_definition_successors", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ArrayElement> successors;

    @ElementCollection
    @CollectionTable(name = "event_resource_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<EventDefinition> resourceDefinitions;

    @ElementCollection
    @CollectionTable(name = "ord_labels_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<Label> documentationLabels;

    @ElementCollection
    @CollectionTable(name = "event_resource_links", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ResourceLink> eventResourceLinks;

    @Column(name = "implementation_standard")
    private String implementationStandard;

    @Column(name = "custom_implementation_standard")
    private String customImplementationStandard;

    @Column(name = "custom_implementation_standard_description")
    private String customImplementationStandardDescription;
    
    @Embedded
    private Extensible extensible;

    @Column(name = "last_update")
    private String lastUpdate;

    @Column(name = "deprecation_date")
    private String deprecationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "package_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private PackageEntity pkg;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tenants_event_bundle_reference",
            joinColumns = {
                    @JoinColumn(name = "event_def_id", referencedColumnName = "id"),
                    @JoinColumn(name = "formation_id", referencedColumnName = "formation_id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "bundle_id", referencedColumnName = "id"),
                    @JoinColumn(name = "formation_id", referencedColumnName = "formation_id"),
            }
    )
    private Set<BundleEntity> consumptionBundles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_product",
            joinColumns = {
                    @JoinColumn(name = "event_definition_id", referencedColumnName = "id"),
                    @JoinColumn(name = "formation_id", referencedColumnName = "formation_id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "product_id", referencedColumnName = "id"),
                    @JoinColumn(name = "formation_id", referencedColumnName = "formation_id"),
            }
    )
    private Set<ProductEntity> products;

    @Column(name = "responsible")
    private String responsible;
}