package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Oauth2ClientCredentialsDetails {
    @Column(name = "token_service_url")
    private String tokenServiceUrl;

    @Column(name = "client_id")
    private String clientId;
}