package com.sap.cloud.cmp.ord.service.storage.model;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity(name = "systemType")
@Table(name = "app_templates")
public class SystemTypeEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "app_templates_business_tenants",
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tenant_id")})
    private Set<BusinessTenantEntity> businessTenants;

    // TODO API Insights: Breaks tenant boundaries
//    @OneToMany(mappedBy = "systemType", fetch = FetchType.LAZY)
//    private Set<SystemInstanceEntity> systemInstances;
}
