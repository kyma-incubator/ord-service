package com.sap.cloud.cmp.ord.service.config;

import org.springframework.beans.factory.annotation.Value;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.sap.cloud.cmp.ord.service.client.DestinationFetcherClient;

@Configuration
public class DestinationFetcherConfig {

    @Value("${destination_fetcher.reload_url}")
    private String reloadUrl;

    @Value("${destination_fetcher.sensitive_data_url}")
    private String sensitiveDataUrl;

    @Value("${destination_fetcher.tenant_header:Tenant}")
    private String tenantHeaderName;

    @Value("${destination_fetcher.sensitive_data_query_param:name}")
    private String sensitiveDataQueryParamName;

    @Value("${destination_fetcher.auth_token_path:/var/run/secrets/kubernetes.io/serviceaccount/token}")
    private String authTokenPath;

    @Value("${destination_fetcher.skip_ssl_validation:false}")
    private Boolean skipSSLValidation;

    @Bean
    public DestinationFetcherClient createDestinationFetcherClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return new DestinationFetcherClient(reloadUrl, sensitiveDataUrl,
            tenantHeaderName, sensitiveDataQueryParamName,
            authTokenPath, createHttpClient(skipSSLValidation));
    }

    private RestTemplate createHttpClient(Boolean skipSSLValidation) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        if (!skipSSLValidation) {
            return new RestTemplate();
        }

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                        .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                        new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }
}
