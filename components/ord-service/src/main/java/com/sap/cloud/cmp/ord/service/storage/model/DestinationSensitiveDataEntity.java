package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

@Entity(name = "sensitiveData")
@Table(name = "tenants_destinations_sensitive_data")
public class DestinationSensitiveDataEntity {

    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID id;

    @Column(name = "sensitive_data")
    private String content;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private DestinationEntity destination;
}
