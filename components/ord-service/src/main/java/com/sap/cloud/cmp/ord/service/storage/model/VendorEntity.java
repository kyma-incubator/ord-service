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
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity(name = "vendor")
@Table(name = "tenants_vendors")
public class VendorEntity {
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Id
    @Column(name = "ord_id", length = 256)
    @NotNull
    private String ordId;

    @Column(name = "title", length = 256)
    @NotNull
    private String title;

    @ElementCollection
    @CollectionTable(name = "partners", joinColumns = @JoinColumn(name = "vendor_id", referencedColumnName = "id"))
    private List<ArrayElement> partners;

    @ElementCollection
    @CollectionTable(name = "ord_tags_vendors", joinColumns = @JoinColumn(name = "vendor_id", referencedColumnName = "id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "ord_labels_vendors", joinColumns = @JoinColumn(name = "vendor_id", referencedColumnName = "id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels_vendors", joinColumns = @JoinColumn(name = "vendor_id", referencedColumnName = "id"))
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
