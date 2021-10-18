package com.sap.cloud.cmp.ord.service.health;

import com.sap.cloud.cmp.ord.service.repository.SchemaRepository;
import com.sap.cloud.cmp.ord.service.storage.model.SchemaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("schemaCompatibility")
public class ReadinessHealthIndicator implements HealthIndicator {
    private static final Logger logger = LoggerFactory.getLogger(ReadinessHealthIndicator.class);
    private final String DEFAULT_SCHEMA_MIGRATION_VERSION = "0";

    private boolean isHealthy = false;

    @Value("${schema.migration_version:0}")
    private String version;

    @Autowired
    private SchemaRepository schemaRepository;

    @Override
    public Health health() {
        if (version.equals(DEFAULT_SCHEMA_MIGRATION_VERSION)) {
            logger.warn("Missing schema migration version. Set to default.");
        }

        if (!isHealthy) {
            SchemaEntity schema = schemaRepository.getByVersionNotNull();
            String currentVersion = schema.getVersion();
            isHealthy = currentVersion.equals(version);
        }

        return isHealthy ? Health.up().build() : Health.down().build();
    }
}
