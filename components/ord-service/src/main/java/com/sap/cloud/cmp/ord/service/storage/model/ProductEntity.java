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
@Table(name = "tenants_products")
public class ProductEntity {
    @javax.persistence.Id
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

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "vendor")
    @NotNull
    @EdmIgnore
    private String vendorReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "vendor", referencedColumnName= "ord_id", insertable = false, updatable = false),
    })
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

    @ElementCollection
    @CollectionTable(name = "correlation_ids_products", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private List<ArrayElement> correlationIds;

    @ElementCollection
    @CollectionTable(name = "ord_tags_products", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "ord_labels_products", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels_products", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private List<Label> documentationLabels;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private Set<PackageEntity> packages;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private Set<APIEntity> apis;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private Set<EventEntity> events;

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
}
