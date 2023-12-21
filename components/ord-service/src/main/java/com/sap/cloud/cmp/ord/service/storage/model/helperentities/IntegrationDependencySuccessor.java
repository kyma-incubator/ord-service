package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "integration_dependencies_successors")
@Entity(name = "integrationDependencySuccessor")
@IdClass(IntegrationDependencySuccessor.class)
public class IntegrationDependencySuccessor implements Serializable {
    @Id
    @Column(name = "integration_dependency_id", length = 256)
    private String integrationDependencyId;

    @Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
