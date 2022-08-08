package com.sap.cloud.cmp.ord.service.token;

import com.sap.cloud.cmp.ord.service.repository.SelfRegisteredRuntimeRepository;

public class SubscriptionHelper {

    private String selfRegKey;
    private String regionKey;
    private String tokenPrefix;
    private SelfRegisteredRuntimeRepository repo;

    public SubscriptionHelper(String selfRegKey, String regionKey, String tokenPrefix, SelfRegisteredRuntimeRepository repo) {
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

    public SelfRegisteredRuntimeRepository getRepo() {
        return repo;
    }
}
