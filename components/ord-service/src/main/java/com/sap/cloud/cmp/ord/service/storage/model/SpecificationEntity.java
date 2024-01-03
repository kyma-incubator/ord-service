package com.sap.cloud.cmp.ord.service.storage.model;

import java.util.UUID;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "specification")
@Table(name = "tenants_specifications")
@EdmIgnore
public class SpecificationEntity {
    @Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID specId;

    @Column(name = "api_def_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID apiDefinitionId;

    @Column(name = "api_spec_format")
    private String apiSpecFormat;

    @Column(name = "event_def_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID eventDefinitionId;

    @Column(name = "event_spec_format")
    private String eventSpecFormat;

    @Column(name = "capability_def_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID capabilityDefinitionId;

    @Column(name = "capability_spec_format")
    private String capabilitySpecFormat;

    @Column(name = "spec_data", length = Integer.MAX_VALUE)
    private String specData;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;

    public String getSpecData() {
        return specData;
    }

    public String getApiSpecFormat() {
        return convertSpecFormat(this.apiSpecFormat);
    }

    public String getEventSpecFormat() {
        return convertSpecFormat(this.eventSpecFormat);
    }

    public String getCapabilitySpecFormat() {
        return convertSpecFormat(this.capabilitySpecFormat);
    }

    private String convertSpecFormat(String format) {
        switch (format) {
            case "JSON":
                format = "application/json";
                break;
            case "YAML":
                format = "text/yaml";
                break;
            case "XML":
                format = "application/xml";
                break;
        }
        return format;
    }
}
