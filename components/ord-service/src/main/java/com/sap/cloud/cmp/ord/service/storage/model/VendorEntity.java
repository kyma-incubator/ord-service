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

@Entity(name = "vendor")
@Table(name = "tenants_vendors")
public class VendorEntity {
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @javax.persistence.Id
    @Column(name = "ord_id", length = 256)
    @NotNull
    private String ordId;

    @Column(name = "title", length = 256)
    @NotNull
    private String title;

    @ElementCollection
    @CollectionTable(name = "partners", joinColumns = @JoinColumn(name = "vendor_id", referencedColumnName= "id"))
    private List<ArrayElement> partners;

    @ElementCollection
    @CollectionTable(name = "ord_labels", joinColumns = @JoinColumn(name = "vendor_id", referencedColumnName= "id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "documentation_labels", joinColumns = @JoinColumn(name = "vendor_id", referencedColumnName= "id"))
    private List<Label> documentationLabels;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    private Set<ProductEntity> products;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    private Set<PackageEntity> packages;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private SystemInstanceEntity systemInstance;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    private String tenant;

    @EdmProtectedBy(name = "provider_tenant_id")
    @EdmIgnore
    @Column(name = "provider_tenant_id", length = 256)
    private String providerTenant;
}
