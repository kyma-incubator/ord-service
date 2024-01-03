package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity(name = "schema")
@Table(name = "schema_migrations")
@EdmIgnore
public class SchemaEntity {
    @Id
    @Column(name = "version")
    private String version;

    @Column(name = "dirty")
    private boolean dirty;

    public String getVersion() {
        return version;
    }

    public boolean isDirty() {
        return dirty;
    }
}
