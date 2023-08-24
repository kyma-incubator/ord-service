package com.sap.cloud.cmp.ord.service.token;

import com.sap.cloud.cmp.ord.service.repository.SelfRegisteredRepository;

public class SubscriptionHelper {

    private String selfRegKey;
    private String regionKey;
    private String tokenPrefix;
    private SelfRegisteredRepository repo;

    public SubscriptionHelper(String selfRegKey, String regionKey, String tokenPrefix, SelfRegisteredRepository repo) {
        this.selfRegKey = selfRegKey;
        this.regionKey = regionKey;
        this.tokenPrefix = tokenPrefix;
        this.repo = repo;
    }

    public String getSelfRegKey() {
        return selfRegKey;
    }

    public String getRegionKey() {
        return regionKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public SelfRegisteredRepository getRepo() {
        return repo;
    }
}
