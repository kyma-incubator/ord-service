package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import java.util.List;
import java.util.UUID;

@Entity(name = "dataProduct")
@Table(name = "tenants_data_products")
public class DataProductEntity {
    @jakarta.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

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

    @Column(name = "ord_id", length = 256, nullable = false)
    private String ordId;

    @Column(name = "local_tenant_id", length = 256)
    private String localId;

    @ElementCollection
    @CollectionTable(name = "correlation_ids_data_products", joinColumns = @JoinColumn(name = "data_product_id", referencedColumnName= "id"))
    private List<ArrayElement> correlationIds;

    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Column(name = "short_description", length = 256)
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "package_id", nullable = false)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
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
    @Column(name = "visibility", nullable = false)
    private String visibility;

    @Column(name = "release_status", nullable = false)
    private String releaseStatus;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "deprecation_date")
    private String deprecationDate;

    @Column(name = "sunset_date")
    private String sunsetDate;

    @ElementCollection
    @CollectionTable(name = "data_product_successors", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ArrayElement> successors;

    @ElementCollection
    @CollectionTable(name = "changelog_entries_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ChangelogEntry> changelogEntries;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "category", nullable = false)
    private String category;

    @ElementCollection
    @CollectionTable(name = "entity_types_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ArrayElement> entityTypes;

    @ElementCollection
    @CollectionTable(name = "input_ports_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<InputPort> inputPorts;

    @ElementCollection
    @CollectionTable(name = "output_ports_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    @Column(name = "output_ports", nullable = false)
    private List<OutputPort> outputPorts;

    @Column(name = "responsible", nullable = false)
    private String responsible;

    @ElementCollection
    @CollectionTable(name = "data_product_links", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<DataProductLink> dataProductLinks;

    @ElementCollection
    @CollectionTable(name = "links_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<Link> links;

    @ElementCollection
    @CollectionTable(name = "industries_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ArrayElement> industry;

    @ElementCollection
    @CollectionTable(name = "line_of_businesses_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ArrayElement> lineOfBusiness;

    @ElementCollection
    @CollectionTable(name = "tags_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<ArrayElement> tags;

    @ElementCollection
    @CollectionTable(name = "ord_labels_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(name = "ord_documentation_labels_data_products", joinColumns = @JoinColumn(name = "data_product_id"))
    private List<Label> documentationLabels;

    @Column(name = "policy_level", length = 256)
    private String policyLevel;

    @Column(name = "custom_policy_level", length = 256)
    private String customPolicyLevel;

    @Column(name = "system_instance_aware")
    private boolean systemInstanceAware;

    @Column(name = "version_value", nullable = false)
    private String version;
}