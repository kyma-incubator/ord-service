package com.sap.cloud.cmp.ord.service.repository;

import com.sap.cloud.cmp.ord.service.storage.model.RootEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;



// JPARepository is not needed in this case because there is not a specific Entity associated with the defined queries.
// However, Spring require that in order to instantiate the repository object in it’s context.
// That’s why we use a random entity just to extend the interface and make the autowiring work.
public interface SelfRegisteredRuntimeRepository extends JpaRepository<RootEntity, Integer> {
    @Query(nativeQuery = true, value = "SELECT l1.runtime_id FROM labels l1 JOIN labels l2 ON l1.runtime_id = l2.runtime_id WHERE l1.runtime_id IS NOT NULL AND l1.runtime_id IN (SELECT id FROM tenant_runtimes WHERE tenant_id = uuid(?1) AND owner = true) AND l1.key = ?2 AND l1.value = to_jsonb(?3) AND l2.key = ?4 AND l2.value = to_jsonb(?5)")
    Set<String> findSelfRegisteredRuntimesByLabels(String providerTenantId, String selfRegKey, String selfRegValue, String regionKey, String regionValue);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tenant_runtime_contexts trc WHERE trc.tenant_id = uuid(?1) AND trc.id IN (SELECT id FROM runtime_contexts WHERE runtime_id = uuid(?2)) AND trc.owner = true")
    int isRuntimeSubscriptionAvailableInTenant(String consumerTenantId, String runtimeId);
}
