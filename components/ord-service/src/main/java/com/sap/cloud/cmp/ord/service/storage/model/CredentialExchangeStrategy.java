package com.sap.cloud.cmp.ord.service.storage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CredentialExchangeStrategy {
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "custom_type", length = Integer.MAX_VALUE)
    private String customType;

    @Column(name = "custom_description", length = Integer.MAX_VALUE)
    private String customDescription;

    @Column(name = "callback_url", length = Integer.MAX_VALUE)
    private String callbackUrl;
}
