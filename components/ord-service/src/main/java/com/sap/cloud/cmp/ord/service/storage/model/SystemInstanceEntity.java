package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "systemInstance")
@Table(name = "tenants_apps")
public class SystemInstanceEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "name", length = 256)
    private String title;

    @Column(name = "base_url", length = 512)
    private String baseUrl;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "system_number", length = 256)
    private String systemNumber;

    @Column(name = "product_type", length = 256)
    private String productType;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<PackageEntity> packages;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<BundleEntity> consumptionBundles;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<ProductEntity> products;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<TombstoneEntity> tombstones;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<VendorEntity> vendors;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    private String tenant;

    @EdmProtectedBy(name = "provider_tenant_id")
    @EdmIgnore
    @Column(name = "provider_tenant_id", length = 256)
    private String providerTenant;

    @ElementCollection
    @CollectionTable(name = "ord_labels", joinColumns = @JoinColumn(name = "application_id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels", joinColumns = @JoinColumn(name = "application_id"))
    private List<Label> documentationLabels;

    @ElementCollection
    @CollectionTable(name = "correlation_ids", joinColumns = @JoinColumn(name = "application_id"))
    private List<ArrayElement> correlationIds;
}
