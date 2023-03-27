package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@EdmIgnore
@Table(name = "output_port_api_reference")
@Entity(name = "outputPortAPIReference")
public class OutputPortAPIReference {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @Column(name = "app_id", length = 256)
    private String appID;

    @Column(name = "port_id", length = 256)
    private String portID;

    @Column(name = "api_id", length = 256)
    private String apiID;

    @Column(name = "min_version", length = 256)
    private String minVersion;
}
