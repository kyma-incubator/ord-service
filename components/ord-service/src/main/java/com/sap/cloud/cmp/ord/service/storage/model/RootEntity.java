package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@EdmIgnore
public class RootEntity {
    @Id
    private Integer id;
}
