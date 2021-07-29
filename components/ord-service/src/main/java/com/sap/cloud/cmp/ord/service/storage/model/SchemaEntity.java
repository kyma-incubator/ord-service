package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity(name = "schema")
@Table(name = "schema_migrations")
public class SchemaEntity {
    @javax.persistence.Id
    @Column(name = "version")
    private BigInteger version;

    @Column(name = "dirty")
    private boolean dirty;

    public BigInteger getVersion() {
        return version;
    }
}
