package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "schema")
@Table(name = "schema_migrations")
public class SchemaEntity {
    @javax.persistence.Id
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
