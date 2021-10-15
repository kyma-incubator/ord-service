package com.sap.cloud.cmp.ord.service.repository;

import com.sap.cloud.cmp.ord.service.storage.model.SpecificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpecRepository extends JpaRepository<SpecificationEntity, UUID> {
    SpecificationEntity getBySpecIdAndApiDefinitionIdAndTenantAndProviderTenant(UUID id, UUID apiDefId, String tenant, String providerTenant);

    SpecificationEntity getBySpecIdAndEventDefinitionIdAndTenantAndProviderTenant(UUID id, UUID apiDefId, String tenant, String providerTenant);
}
