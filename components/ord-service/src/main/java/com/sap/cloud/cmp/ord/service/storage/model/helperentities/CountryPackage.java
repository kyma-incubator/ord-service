package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "countries_packages")
@Entity(name = "countryPackage")
@IdClass(CountryPackage.class)
public class CountryPackage implements Serializable {
    @Id
    @Column(name = "package_id", length = 256)
    private String packageID;

    @Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
