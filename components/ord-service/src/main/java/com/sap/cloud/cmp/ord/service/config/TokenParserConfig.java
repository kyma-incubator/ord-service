package com.sap.cloud.cmp.ord.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sap.cloud.cmp.ord.service.repository.SelfRegisteredRuntimeRepository;
import com.sap.cloud.cmp.ord.service.token.SubscriptionHelper;
import com.sap.cloud.cmp.ord.service.token.TokenParser;

@Configuration
public class TokenParserConfig {

    @Value("${subscription.provider_label_key:subscriptionProviderId}")
    private String selfRegKey;

    @Value("${subscription.region_key:region}")
    private String regionKey;

    @Value("${subscription.token_prefix:prefix-}")
    private String tokenPrefix;

    @Bean
    public TokenParser tokenParser(SelfRegisteredRuntimeRepository repo) {
        return new TokenParser(new SubscriptionHelper(selfRegKey, regionKey, tokenPrefix, repo));
    }
}
