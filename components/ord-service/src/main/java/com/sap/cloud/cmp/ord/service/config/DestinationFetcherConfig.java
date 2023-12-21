package com.sap.cloud.cmp.ord.service.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${http.headers.correlationId}")
    private String correlationIdHeader;

    @Value("${destination_fetcher.sensitive_data_query_param:name}")
    private String sensitiveDataQueryParamName;

    @Value("${destination_fetcher.auth_token_path:/var/run/secrets/kubernetes.io/serviceaccount/token}")
    private String authTokenPath;

    @Value("${destination_fetcher.skip_ssl_validation:false}")
    private Boolean skipSSLValidation;

    @Bean
    public DestinationFetcherClient createDestinationFetcherClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return new DestinationFetcherClient(reloadUrl, sensitiveDataUrl,
            tenantHeaderName, correlationIdHeader, sensitiveDataQueryParamName,
            authTokenPath, createHttpClient(skipSSLValidation));
    }

    private RestTemplate createHttpClient(Boolean skipSSLValidation) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        if (!skipSSLValidation) {
            return new RestTemplate();
        }

        final TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        final SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
 
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
            .register("https", sslsf)
            .build();

        final BasicHttpClientConnectionManager connectionManager = 
            new BasicHttpClientConnectionManager(socketFactoryRegistry);        
        
        final CloseableHttpClient httpClient =  HttpClients.custom()
            .setConnectionManager(connectionManager)
            .build();

        final HttpComponentsClientHttpRequestFactory requestFactory =
                        new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(requestFactory);
    }
}
