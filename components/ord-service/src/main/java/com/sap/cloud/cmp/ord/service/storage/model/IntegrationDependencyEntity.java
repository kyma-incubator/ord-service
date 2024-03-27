package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.OneToMany;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.validation.constraints.NotNull;

@Entity(name = "integrationDependency")
@Table(name = "tenants_integration_dependencies")
public class IntegrationDependencyEntity {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

    @JoinColumns({
            @JoinColumn(name = "app_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Column(name = "ord_id", length = 256)
    private String ordId;

    @Column(name = "local_tenant_id", length = 256)
    private String localId;

    @ElementCollection
    @CollectionTable(name = "correlation_ids_integration_dependencies", joinColumns = @JoinColumn(name = "integration_dependency_id", referencedColumnName= "id"))
    private List<ArrayElement> correlationIds;

    @Column(name = "title", length = 256)
    private String title;

    @Column(name = "short_description", length = 256)
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "package_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    @NotNull
    private UUID partOfPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "package_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "formation_id", referencedColumnName = "formation_id", insertable = false, updatable = false),
    })
    private PackageEntity pkg;

    @Column(name = "last_update")
    private String lastUpdate;

    @EdmProtectedBy(name = "visibility_scope")
    @Column(name = "visibility")
    private String visibility;

    @Column(name = "release_status")
    @NotNull
    private String releaseStatus;

    @Column(name = "sunset_date")
    private String sunsetDate;

    @ElementCollection
    @CollectionTable(name = "integration_dependencies_successors", joinColumns = @JoinColumn(name = "integration_dependency_id"))
    private List<ArrayElement> successors;

    @Column(name = "mandatory")
    @NotNull
    private boolean mandatory;

    @ElementCollection
    @CollectionTable(name = "related_integration_dependencies", joinColumns = @JoinColumn(name = "integration_dependency_id"))
    private List<ArrayElement> relatedIntegrationDependencies;

    @ElementCollection
    @CollectionTable(name = "links_integration_dependencies", joinColumns = @JoinColumn(name = "integration_dependency_id"))
    private List<Link> links;

    @ElementCollection
    @CollectionTable(name = "tags_integration_dependencies", joinColumns = @JoinColumn(name = "integration_dependency_id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "ord_labels_integration_dependencies", joinColumns = @JoinColumn(name = "integration_dependency_id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels_integration_dependencies", joinColumns = @JoinColumn(name = "integration_dependency_id"))
    private List<Label> documentationLabels;

    @Column(name = "version_value")
    private String version;

    @OneToMany(mappedBy = "integrationDependency", fetch = FetchType.LAZY)
    private Set<AspectEntity> aspects;
}
