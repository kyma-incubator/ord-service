package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

/**
 * Once we introduce ORD Aggregator APIDefinitions should be fetched as Specifications.
 * Therefore Compass should support multiple specs per API.
 * In order to achieve that we will create a new specifications table which will unify api/event specifications.
 */
@Entity(name = "apiSpecification")
@Table(name = "api_definitions")
@EdmIgnore
public class APISpecificationEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID apiDefinitionId;

    @Column(name = "spec_data", length = Integer.MAX_VALUE)
    private String specData;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;


    public UUID getApiDefinitionId() {
        return apiDefinitionId;
    }

    public String getSpecData() {
        return specData;
    }
}
