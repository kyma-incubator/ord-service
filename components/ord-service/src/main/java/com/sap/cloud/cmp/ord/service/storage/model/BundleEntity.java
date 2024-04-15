package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ManyToMany;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.validation.constraints.NotNull;

@Entity(name = "consumptionBundle")
@Table(name = "tenants_bundles")
public class BundleEntity {
    @jakarta.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "ord_id", length = 256, nullable = false)
    private String ordId;

    @Column(name = "name", length = 256, nullable = false)
    private String title;

    @Column(name = "short_description", length = 256)
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "version")
    private String version;

    @Column(name = "local_tenant_id", length = 256)
    private String localId;

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
    @CollectionTable(name = "ord_tags_bundles", joinColumns = @JoinColumn(name = "bundle_id", referencedColumnName = "id"))
    private List<ArrayElement> tags;

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

    @Column(name = "last_update")
    private String lastUpdate;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "app_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private SystemInstanceEntity systemInstance;

    @ManyToMany(mappedBy = "consumptionBundles", fetch = FetchType.LAZY)
    private Set<APIEntity> apis;

    @ManyToMany(mappedBy = "consumptionBundles", fetch = FetchType.LAZY)
    private Set<EventEntity> events;
    
    @ManyToMany(mappedBy = "consumptionBundles", fetch = FetchType.LAZY)
    private Set<DestinationEntity> destinations;
}