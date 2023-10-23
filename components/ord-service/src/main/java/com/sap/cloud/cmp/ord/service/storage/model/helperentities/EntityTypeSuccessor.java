package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "entity_type_successors")
@Entity(name = "entity_type_successor")
@IdClass(EntityTypeSuccessor.class)
public class EntityTypeSuccessor implements Serializable {
    @javax.persistence.Id
    @Column(name = "entity_type_id", length = 256)
    private String entityTypeID;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
