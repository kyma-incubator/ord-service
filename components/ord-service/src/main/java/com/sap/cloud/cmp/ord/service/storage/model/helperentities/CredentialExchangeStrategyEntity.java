package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "credential_exchange_strategies")
@Entity(name = "credentialExchangeStrategy")
@IdClass(CredentialExchangeStrategyEntity.class)
public class CredentialExchangeStrategyEntity implements Serializable {
    @Id
    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @Id
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Id
    @Column(name = "custom_type", length = Integer.MAX_VALUE)
    private String customType;

    @Id
    @Column(name = "custom_description", length = Integer.MAX_VALUE)
    private String customDescription;

    @Id
    @Column(name = "callback_url", length = Integer.MAX_VALUE)
    private String callbackUrl;
}
