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

@Entity(name = "api")
@Table(name = "tenants_apis")
public class APIEntity {
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

    @Column(name = "short_description", length = 255)
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "version_value")
    private String version;

    @Column(name = "visibility")
    private String visibility;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "system_instance_aware")
    private boolean systemInstanceAware;

    @Column(name = "package_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    @NotNull
    private UUID partOfPackage;

    @ElementCollection
    @CollectionTable(name = "api_bundle_reference", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ConsumptionBundleReference> partOfConsumptionBundles;

    @Column(name = "api_protocol")
    private String apiProtocol;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "countries", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ArrayElement> countries;

    @ElementCollection
    @CollectionTable(name = "api_resource_definitions", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<APIDefinition> resourceDefinitions;

    @ElementCollection
    @CollectionTable(name = "links", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<Link> links;

    @ElementCollection
    @CollectionTable(name = "line_of_businesses", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ArrayElement> lineOfBusiness;

    @ElementCollection
    @CollectionTable(name = "industries", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ArrayElement> industry;

    @ElementCollection
    @CollectionTable(name = "api_resource_links", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<APIResourceLink> apiResourceLinks;

    @Column(name = "release_status")
    @NotNull
    private String releaseStatus;

    @Column(name = "sunset_date")
    private String sunsetDate;

    @ElementCollection
    @CollectionTable(name = "api_definition_successors", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ArrayElement> successors;

    @ElementCollection
    @CollectionTable(name = "changelog_entries", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ChangelogEntry> changelogEntries;

    @ElementCollection
    @CollectionTable(name = "target_urls", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ArrayElement> entryPoints;

    @ElementCollection
    @CollectionTable(name = "ord_labels", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "api_definition_extensible", joinColumns = @JoinColumn(name = "api_definition_id"))
    private List<ExtensibleEntity> extensible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    private PackageEntity pkg;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "bundle_references",
            joinColumns = @JoinColumn(name = "api_def_id"),
            inverseJoinColumns = @JoinColumn(name = "bundle_id"))
    private Set<BundleEntity> consumptionBundles;

    @ManyToMany
    @JoinTable(
            name = "api_product",
            joinColumns = {@JoinColumn(name = "api_definition_id", referencedColumnName= "id"),@JoinColumn(name = "app_id", referencedColumnName= "app_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName= "ord_id"), @JoinColumn(name = "app_id", referencedColumnName= "app_id")})
    private Set<ProductEntity> products;

    @Column(name = "implementation_standard")
    private String implementationStandard;

    @Column(name = "custom_implementation_standard")
    private String customImplementationStandard;

    @Column(name = "custom_implementation_standard_description")
    private String customImplementationStandardDescription;
}