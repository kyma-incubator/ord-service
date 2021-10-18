package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "specification")
@Table(name = "tenants_specifications")
@EdmIgnore
public class SpecificationEntity {
    @javax.persistence.Id
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

    @Column(name = "spec_data", length = Integer.MAX_VALUE)
    private String specData;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    private String tenant;

    @EdmProtectedBy(name = "provider_tenant_id")
    @EdmIgnore
    @Column(name = "provider_tenant_id", length = 256)
    private String providerTenant;

    public String getSpecData() {
        return specData;
    }

    public String getApiSpecFormat() {
        return convertSpecFormat(this.apiSpecFormat);
    }

    public String getEventSpecFormat() {
        return convertSpecFormat(this.eventSpecFormat);
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
