package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "industries_api_definitions")
@Entity(name = "industryAPIDefinition")
@IdClass(IndustryAPIDefinition.class)
public class IndustryAPIDefinition implements Serializable {
    @javax.persistence.Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}