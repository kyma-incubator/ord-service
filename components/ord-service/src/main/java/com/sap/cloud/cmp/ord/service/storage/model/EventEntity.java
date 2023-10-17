package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "event")
@Table(name = "tenants_events")
public class EventEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "ord_id", length = 256)
    private String ordId;

    @Column(name = "name", length = 256)
    @NotNull
    private String title;

    @Column(name = "short_description", length = 256)
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @EdmProtectedBy(name = "visibility_scope")
    @Column(name = "visibility")
    private String visibility;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "version_value")
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

    @ElementCollection
    @CollectionTable(name = "changelog_entries_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ChangelogEntry> changelogEntries;

    @Column(name = "package_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    @NotNull
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

    @Column(name = "release_status")
    @NotNull
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    private PackageEntity pkg;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "bundle_references",
            joinColumns = @JoinColumn(name = "event_def_id"),
            inverseJoinColumns = @JoinColumn(name = "bundle_id"))
    private Set<BundleEntity> consumptionBundles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_product",
            joinColumns = {@JoinColumn(name = "event_definition_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private Set<ProductEntity> products;
}