package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.cloud.cmp.ord.service.storage.model.SystemInstanceEntity;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Entity(name = "port")
@Table(name = "tenants_ports")
public class PortEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @EdmIgnore
    @Column(name = "data_product_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID dataProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_product_id", insertable = false, updatable = false)
    private DataProductEntity dataProduct;

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "port_type", length = 256)
    private String portType;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "producer_cardinality", length = 256)
    private String producerCardinality;

    @Column(name = "disabled")
    private boolean disabled;

    @EdmIgnore
    @Column(name = "app_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID appId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private SystemInstanceEntity systemInstance;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;

    @EdmProtectedBy(name = "formation_scope")
    @EdmIgnore
    @Column(name = "formation_id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID formationID;

   @ManyToMany(mappedBy = "port", fetch = FetchType.LAZY)
    private Set<APIEntity> apis;

   @ManyToMany(mappedBy = "port", fetch = FetchType.LAZY)
   private Set<EventEntity> events;
}

