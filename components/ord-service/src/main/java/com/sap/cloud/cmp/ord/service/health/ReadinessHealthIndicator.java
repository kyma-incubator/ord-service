package com.sap.cloud.cmp.ord.service.health;

import com.sap.cloud.cmp.ord.service.repository.SchemaRepository;
import com.sap.cloud.cmp.ord.service.storage.model.SchemaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component("readiness")
public class ReadinessHealthIndicator implements HealthIndicator {
    private boolean isHealthy = false;
    private String version;
    private boolean schemaIsCompatible = false;

    @Autowired
    private SchemaRepository schemaRepository;

    public ReadinessHealthIndicator() {
        version = System.getenv().get("SCHEMA_MIGRATION_VERSION");
    }

    @Override
    public Health health() {
        if (!schemaIsCompatible) {
            SchemaEntity schema = schemaRepository.get();
            BigInteger currentVersion = schema.getVersion();

            schemaIsCompatible = version.equals(currentVersion.toString());
        }

        return isHealthy ? Health.up().build() : Health.down().build();
    }
}
