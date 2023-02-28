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

@Entity(name = "consumptionBundle")
@Table(name = "tenants_bundles")
public class BundleEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "ord_id", length = 256)
    @NotNull
    private String ordId;

    @Column(name = "name", length = 256)
    @NotNull
    private String title;

    @Column(name = "short_description", length = 256)
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

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

    @ElementCollection
    @CollectionTable(name = "links_bundles", joinColumns = @JoinColumn(name = "bundle_id"))
    private List<Link> links;

    @ElementCollection
    @CollectionTable(name = "ord_labels_bundles", joinColumns = @JoinColumn(name = "bundle_id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels_bundles", joinColumns = @JoinColumn(name = "bundle_id"))
    private List<Label> documentationLabels;

    @ElementCollection
    @CollectionTable(name = "credential_exchange_strategies", joinColumns = @JoinColumn(name = "bundle_id"))
    private List<CredentialExchangeStrategy> credentialExchangeStrategies;

    @ElementCollection
    @CollectionTable(name = "correlation_ids_bundles", joinColumns = @JoinColumn(name = "bundle_id", referencedColumnName= "id"))
    private List<ArrayElement> correlationIds;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private SystemInstanceEntity systemInstance;

    @ManyToMany(mappedBy = "consumptionBundles", fetch = FetchType.LAZY)
    private Set<APIEntity> apis;

    @ManyToMany(mappedBy = "consumptionBundles", fetch = FetchType.LAZY)
    private Set<EventEntity> events;
    
    @ManyToMany(mappedBy = "consumptionBundles", fetch = FetchType.LAZY)
    private Set<DestinationEntity> destinations;
}