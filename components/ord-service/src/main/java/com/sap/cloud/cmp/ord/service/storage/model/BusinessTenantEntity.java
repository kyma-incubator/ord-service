package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

// TODO API Insights: This should be hidden, we cannot allow anyone to dump all business tenants by calling /businessTenants
@Entity(name = "businessTenant")
@Table(name = "business_tenants")
public class BusinessTenantEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID internalId;

    @Column(name = "external_tenant")
    private String Id;

    @Column(name = "external_name", length = 256)
    private String name;

    @Column(name = "type", length = 256)
    private String type;

    @Column(name = "license_type", length = Integer.MAX_VALUE)
    private String licenseType;

    @Column(name = "region", length = 256)
    private String region;

    @Column(name = "subdomain", length = 256)
    private String subdomain;

    @Column(name = "parent_external_tenant", length = 256)
    private String parentId;

    // TODO API Insights: Breaks tenant boundaries
//    @ManyToMany(mappedBy = "businessTenants", fetch = FetchType.LAZY)
//    private Set<SystemTypeEntity> systemTypes;
//
//    @ManyToMany(mappedBy = "businessTenants", fetch = FetchType.LAZY)
//    private Set<SystemInstanceEntity> systemInstances;

}
