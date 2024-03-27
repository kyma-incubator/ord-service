package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.Set;
import java.util.UUID;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity(name = "destination")
@Table(name = "tenants_destinations")
public class DestinationEntity {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;

    @EdmProtectedBy(name = "destination_tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;

    @Column(name = "sensitive_data", length = Integer.MAX_VALUE)
    private String sensitiveData;

    @EdmIgnore
    @Column(name = "formation_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID formationID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tenants_destinations",
            joinColumns = {
                    @JoinColumn(name = "id", referencedColumnName = "id"),
                    @JoinColumn(name = "formation_id", referencedColumnName = "formation_id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "bundle_id", referencedColumnName = "id"),
                    @JoinColumn(name = "formation_id", referencedColumnName = "formation_id"),
            }
    )
    private Set<BundleEntity> consumptionBundles;
}
