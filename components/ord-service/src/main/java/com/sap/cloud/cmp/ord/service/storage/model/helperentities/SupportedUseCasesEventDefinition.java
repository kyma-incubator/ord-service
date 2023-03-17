package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "ord_supported_use_cases_event_definitions")
@Entity(name = "supportedUseCasesEventDefinition")
@IdClass(SupportedUseCasesEventDefinition.class)
public class SupportedUseCasesEventDefinition implements Serializable {
    @javax.persistence.Id
    @Column(name = "event_definition_id", length = 256)
    private String eventDefinitionID;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
