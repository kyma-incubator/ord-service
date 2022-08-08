package com.sap.cloud.cmp.ord.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sap.cloud.cmp.ord.service.client.DestinationFetcherClient;

@Configuration
public class DestinationFetcherConfig {

    @Value("${destination_fetcher.reload_url}")
    private String reloadUrl;

    @Value("${destination_fetcher.sensitive_data_url}")
    private String sensitiveDataUrl;

    @Value("${destination_fetcher.user_context_header:user_context}")
    private String userContextHeader;

    @Bean
    public DestinationFetcherClient createDestinationFetcherClient() {
        return new DestinationFetcherClient(reloadUrl, sensitiveDataUrl, userContextHeader);
    }
}
