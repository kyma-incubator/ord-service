package com.sap.cloud.cmp.ord.service.repository;

import java.util.List;
import java.util.UUID;

import com.sap.cloud.cmp.ord.service.storage.model.APISpecificationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiSpecRepository extends JpaRepository<APISpecificationEntity, UUID> {
    public APISpecificationEntity getByApiDefinitionIdAndTenant(UUID id, UUID tenant);
}
