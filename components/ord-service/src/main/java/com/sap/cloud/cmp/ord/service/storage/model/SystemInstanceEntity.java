package com.sap.cloud.cmp.ord.service.storage.model;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "systemInstance")
@Table(name = "applications")
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

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<PackageEntity> packages;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<PackageEntity> consumptionBundles;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<PackageEntity> products;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<PackageEntity> tombstones;

    @OneToMany(mappedBy = "systemInstance", fetch = FetchType.LAZY)
    private Set<PackageEntity> vendors;

    @ElementCollection
    @CollectionTable(name = "ord_labels", joinColumns = @JoinColumn(name = "application_id"))
    private List<Label> labels;
}
