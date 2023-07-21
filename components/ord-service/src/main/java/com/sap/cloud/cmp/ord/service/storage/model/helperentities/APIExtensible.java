package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "api_definition_extensible")
@Entity(name = "apiExtensible")
@IdClass(APIExtensible.class)
public class APIExtensible implements Serializable {
    @Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @Id
    @Column(name = "supported")
    private String supported;

    @Id
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;
}
