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

@Entity(name = "product")
@Table(name = "products")
public class ProductEntity {
    @javax.persistence.Id
    @Column(name = "ord_id", length = 256)
    @NotNull
    private String ordId;

    @Column(name = "title", length = 256)
    @NotNull
    private String title;

    @Column(name = "short_description", length = 255)
    @NotNull
    private String shortDescription;

    @Column(name = "vendor")
    @NotNull
    @EdmIgnore
    private String vendorReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor", insertable = false, updatable = false)
    private VendorEntity vendor;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private SystemInstanceEntity systemInstance;

    @Column(name = "parent", length = 256)
    private String parent;

    @Column(name = "sap_ppms_object_id", length = 256)
    private String sapPpmsObjectId;

    @ElementCollection
    @CollectionTable(name = "ord_labels", joinColumns = @JoinColumn(name = "product_id"))
    private List<Label> labels;

    @ManyToMany(mappedBy = "products")
    private Set<PackageEntity> packages;

    @ManyToMany(mappedBy = "products")
    private Set<APIEntity> apis;

    @ManyToMany(mappedBy = "products")
    private Set<EventEntity> events;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;
}
