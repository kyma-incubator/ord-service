package com.sap.cloud.cmp.ord.service.storage.model;


import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Entity(name="aspect")
@Table(name="tenants_aspects")
public class AspectEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

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

    @EdmIgnore
    @Column(name = "integration_dependency_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID integrationDependencyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integration_dependency_id", insertable = false, updatable = false)
    private IntegrationDependencyEntity integrationDependency;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "mandatory")
    @NotNull
    private boolean mandatory;

    @Column(name = "support_multiple_providers")
    private boolean supportMultipleProviders;

    @OneToMany(mappedBy = "aspect", fetch = FetchType.LAZY)
    private Set<AspectAPIResource> aspectApiResources;

    @OneToMany(mappedBy = "aspect", fetch = FetchType.LAZY)
    private Set<AspectEventResource> aspectEventResources;
}
