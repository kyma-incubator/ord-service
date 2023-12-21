package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "ord_labels_integration_dependencies")
@Entity(name = "labelIntegrationDependency")
@IdClass(LabelEntityIntegrationDependency.class)
public class LabelEntityIntegrationDependency implements Serializable {
    @Id
    @Column(name = "integration_dependency_id", length = 256)
    private String integrationDependencyId;

    @Id
    @Column(name = "key", length = Integer.MAX_VALUE)
    private String key;

    @Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
