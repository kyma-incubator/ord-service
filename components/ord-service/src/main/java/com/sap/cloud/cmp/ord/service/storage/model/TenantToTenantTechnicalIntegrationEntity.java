package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.TypeConverter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "tenantToTenantTechnicalIntegration")
@Table(name = "tenant_to_tenant_technical_integrations")
public class TenantToTenantTechnicalIntegrationEntity {
    @javax.persistence.Id
    @Column(name = "id")
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID Id;

    @EdmProtectedBy(name = "tenant_id")
    @EdmIgnore
    @Column(name = "tenant_id", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID tenant;

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "type", length = 256)
    private String type;

    @EdmIgnore
    @Column(name = "sender", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender", insertable = false, updatable = false)
    private SystemInstanceEntity sender;

    @EdmIgnore
    @Column(name = "receiver", length = 256)
    @Convert("uuidConverter")
    @TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    private UUID receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", insertable = false, updatable = false)
    private SystemInstanceEntity receiver;

    @ElementCollection
    @CollectionTable(name = "oauth2_client_credentials_details", joinColumns = @JoinColumn(name = "tech_int_id"))
    private List<Oauth2ClientCredentialsDetails> oauth2ClientCredentialsDetails;
}
