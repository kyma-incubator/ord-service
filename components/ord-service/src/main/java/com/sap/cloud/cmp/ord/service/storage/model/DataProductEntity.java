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

@Entity(name = "dataProduct")
@Table(name = "tenants_data_products")
public class DataProductEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "ord_id", length = 256)
    private String ordId;

    @Column(name = "local_id", length = 256)
    private String localId;

    @Column(name = "title", length = 256)
    private String title;

    @Column(name = "short_description", length = 256)
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "version", length = Integer.MAX_VALUE)
    private String version;

    @Column(name = "release_status")
    private String releaseStatus;

    @EdmProtectedBy(name = "visibility_scope")
    @Column(name = "visibility")
    private String visibility;

    @Column(name = "package_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID packageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    private PackageEntity pkg;

    @ElementCollection
    @CollectionTable(name = "ord_data_products_tags", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "ord_data_products_industry", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ArrayElement> industry;

    @ElementCollection
    @CollectionTable(name = "ord_data_products_line_of_business", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ArrayElement> lineOfBusiness;

    @Column(name = "product_type")
    @NotNull
    private String productType;

    @Column(name = "data_product_owner")
    @NotNull
    private String dataProductOwner;

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

    @OneToMany(mappedBy = "dataProduct", fetch = FetchType.LAZY)
    private Set<InputPortEntity> inputPorts;

    @OneToMany(mappedBy = "dataProduct", fetch = FetchType.LAZY)
    private Set<OutputPortEntity> outputPorts;
}
