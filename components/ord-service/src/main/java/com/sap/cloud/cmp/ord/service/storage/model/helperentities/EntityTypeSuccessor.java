package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "entity_type_successors")
@Entity(name = "entity_type_successor")
@IdClass(EntityTypeSuccessor.class)
public class EntityTypeSuccessor implements Serializable {
    @Id
    @Column(name = "entity_type_id", length = 256)
    private String entityTypeID;

    @Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
