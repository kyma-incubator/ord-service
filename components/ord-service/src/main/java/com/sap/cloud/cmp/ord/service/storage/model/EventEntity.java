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
    @CollectionTable(name = "ord_hierarchy_event_definitions", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<ArrayElement> hierarchy;

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

    @ElementCollection
    @CollectionTable(name = "event_api_definition_extensible", joinColumns = @JoinColumn(name = "event_definition_id"))
    private List<Extensible> extensible;

    // Currently this relation is a bit misleading because it is not ManyToMany but ManyToOne (1 Event can be part of only 1 Port while 1 Port can have many Events)
    // @ManyToOne seems to not be working properly when in @JoinTable are added several join columns, that's why we fallbacked to the @ManyToMany annotation. The relation itself remains ManyToOne but is modelled as ManyToMany so that the ORD Svc can work. That's why the field is named 'port' instead of 'ports'.
    // In the future we can research whether we can use the right annotation with several join columns or model the relation b/w Events and Ports not in a reference db table but add port_id in the events db table (then the ManyToOne annotation will work correctly)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "input_port_event_reference",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "port_id"))
    private Set<InputPortEntity> inputPort;

    // Currently this relation is a bit misleading because it is not ManyToMany but ManyToOne (1 Event can be part of only 1 Port while 1 Port can have many Events)
    // @ManyToOne seems to not be working properly when in @JoinTable are added several join columns, that's why we fallbacked to the @ManyToMany annotation. The relation itself remains ManyToOne but is modelled as ManyToMany so that the ORD Svc can work. That's why the field is named 'port' instead of 'ports'.
    // In the future we can research whether we can use the right annotation with several join columns or model the relation b/w Events and Ports not in a reference db table but add port_id in the events db table (then the ManyToOne annotation will work correctly)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "output_port_event_reference",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "port_id"))
    private Set<OutputPortEntity> outputPort;

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