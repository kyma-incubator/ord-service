package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "ord_supported_use_cases_api_definitions")
@Entity(name = "supportedUseCasesAPIDefinition")
@IdClass(SupportedUseCasesAPIDefinition.class)
public class SupportedUseCasesAPIDefinition implements Serializable {
    @Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefinitionID;

    @Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
