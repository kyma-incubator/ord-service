package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "ord_hierarchy_api_definitions")
@Entity(name = "hierarchyAPIDefinition")
@IdClass(HierarchyAPIDefinition.class)
public class HierarchyAPIDefinition implements Serializable {
    @javax.persistence.Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefinitionID;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
