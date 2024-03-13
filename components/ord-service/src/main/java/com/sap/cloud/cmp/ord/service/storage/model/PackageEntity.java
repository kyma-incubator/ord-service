package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.validation.constraints.NotNull;

@Entity(name = "package")
@Table(name = "tenants_packages")
public class PackageEntity {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "ord_id", length = 256)
    @NotNull
    private String ordId;

    @Column(name = "title", length = 256)
    @NotNull
    private String title;

    @Column(name = "short_description", length = 256)
    @NotNull
    private String shortDescription;

    @Column(name = "policy_level", length = 256)
    @NotNull
    private String policyLevel;

    @Column(name = "custom_policy_level", length = 256)
    private String customPolicyLevel;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "version")
    private String version;

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
    @CollectionTable(name = "package_links", joinColumns = @JoinColumn(name = "package_id"))
    private List<PackageLink> packageLinks;

    @ElementCollection
    @CollectionTable(name = "links_packages", joinColumns = @JoinColumn(name = "package_id"))
    private List<Link> links;

    @Column(name = "licence_type", length = 256)
    private String licenseType;

    @Column(name = "support_info", length = Integer.MAX_VALUE)
    private String supportInfo;

    @Column(name = "vendor")
    @EdmIgnore
    private String vendorReference;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "vendor", referencedColumnName= "ord_id", insertable = false, updatable = false)
//    private VendorEntity vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "vendor", referencedColumnName = "ord_id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private VendorEntity vendor;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "app_id", insertable = false, updatable = false)
//    private SystemInstanceEntity systemInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "app_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
          //  @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id", insertable = false, updatable = false),
    })
    private SystemInstanceEntity systemInstance;

    @ElementCollection
    @CollectionTable(name = "tags_packages", joinColumns = @JoinColumn(name = "package_id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "countries_packages", joinColumns = @JoinColumn(name = "package_id"))
    private List<ArrayElement> countries;

    @ElementCollection
    @CollectionTable(name = "line_of_businesses_packages", joinColumns = @JoinColumn(name = "package_id"))
    private List<ArrayElement> lineOfBusiness;

    @ElementCollection
    @CollectionTable(name = "industries_packages", joinColumns = @JoinColumn(name = "package_id"))
    private List<ArrayElement> industry;

    @ElementCollection
    @CollectionTable(name = "ord_labels_packages", joinColumns = @JoinColumn(name = "package_id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels_packages", joinColumns = @JoinColumn(name = "package_id"))
    private List<Label> documentationLabels;

    @Column(name = "runtime_restriction", length = Integer.MAX_VALUE)
    private String runtimeRestriction;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "package_product",
//            joinColumns = {@JoinColumn(name = "package_id")},
//            inverseJoinColumns = {@JoinColumn(name = "product_id")})
//    private Set<ProductEntity> products;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "package_product_formation",
            joinColumns = {
                    @JoinColumn(name = "package_id", referencedColumnName = "id"),
                    @JoinColumn(name = "formation_id", referencedColumnName = "formation_id"),
                    //@JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "product_id", referencedColumnName = "id"),
                    @JoinColumn(name = "formation_id", referencedColumnName = "formation_id"),
                    //@JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id"),
            }
    )
    private Set<ProductEntity> products;

    @OneToMany(mappedBy = "pkg", fetch = FetchType.LAZY)
    private Set<APIEntity> apis;

    @OneToMany(mappedBy = "pkg", fetch = FetchType.LAZY)
    private Set<EventEntity> events;

    @OneToMany(mappedBy = "pkg", fetch = FetchType.LAZY) 
    private Set<EntityTypeEntity> entityTypes;

    @OneToMany(mappedBy = "pkg", fetch = FetchType.LAZY)
    private Set<CapabilityEntity> capabilities;

    @OneToMany(mappedBy = "pkg", fetch = FetchType.LAZY)
    private Set<IntegrationDependencyEntity> integrationDependencies;

    @OneToMany(mappedBy = "pkg", fetch = FetchType.LAZY)
    private Set<DataProductEntity> dataProducts;
}