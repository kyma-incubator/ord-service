package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "api_definition_successors")
@Entity(name = "api_definition_successor")
@IdClass(APISuccessor.class)
public class APISuccessor implements Serializable {
    @Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
