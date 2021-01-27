package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CredentialsRequestStrategy {
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "callback_url", length = Integer.MAX_VALUE)
    private String callbackUrl;
}
