package com.sap.cloud.cmp.ord.service.repository;

import java.util.UUID;

import com.sap.cloud.cmp.ord.service.storage.model.APISpecificationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiSpecRepository extends JpaRepository<APISpecificationEntity, UUID> {
    APISpecificationEntity getByApiDefinitionIdAndTenant(UUID id, UUID tenant);
}
