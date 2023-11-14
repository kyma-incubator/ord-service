package com.sap.cloud.cmp.ord.service.config;

import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.server.api.processor.ErrorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sap.olingo.jpa.metadata.api.JPAEdmMetadataPostProcessor;
import com.sap.olingo.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import com.sap.olingo.jpa.processor.core.api.JPAErrorProcessorWrapper;
import com.sap.olingo.jpa.processor.core.api.JPAODataServiceContext;
import com.sap.olingo.jpa.processor.core.api.JPAODataSessionContextAccess;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@ComponentScan
public class ODataJPAConfiguration {

    @Value("${odata.jpa.punit_name}")
    private String punit;

    @Value("${odata.jpa.root_packages}")
    private String rootPackages;

    @Value("${odata.jpa.request_mapping_path}")
    private String requestMappingPath;

    @Value("${odata.api.version}")
    private String odataApiVersion;

    @Bean
    public JPAODataSessionContextAccess sessionContext(
        @Autowired final EntityManagerFactory emf, 
        @Autowired final JPAEdmMetadataPostProcessor jpaEdmMetadataPostProcessor, 
        @Autowired final ErrorProcessor errorProcessor,
        @Autowired final JPAEdmNameBuilder jpaEdmNameBuilder) throws ODataException {

        return JPAODataServiceContext.with()
                .setPUnit(punit)
                .setEntityManagerFactory(emf)
                .setTypePackage(rootPackages)
                .setRequestMappingPath(requestMappingPath)
                .setMetadataPostProcessor(jpaEdmMetadataPostProcessor)
                .setErrorProcessor(errorProcessor)
                .setEdmNameBuilder(jpaEdmNameBuilder)
                //.setDatabaseProcessor(new JPAPostgresDatabaseProcessorImpl()) // Enable only if search query is necessary.
                .build();
    }

    @Bean
    public JPAEdmMetadataPostProcessor jpaEdmMetadataPostProcessor() {
        return new CustomJPAEdmMetadataPostProcessor(this.odataApiVersion);
    }

    @Bean
    public ErrorProcessor jpaErrorProcessor() {
        return new JPAErrorProcessorWrapper(new CustomErrorProcessor());
    }
    
    @Bean
    public JPAEdmNameBuilder jPAEdmNameBuilder() {
        return new CustomJPAEdmNameBuilder(punit);
    }

}
