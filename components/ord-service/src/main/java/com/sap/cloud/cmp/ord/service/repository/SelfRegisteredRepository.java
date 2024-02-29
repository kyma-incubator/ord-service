package com.sap.cloud.cmp.ord.service.repository;

import com.sap.cloud.cmp.ord.service.storage.model.RootEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;



// JPARepository is not needed in this case because there is not a specific Entity associated with the defined queries.
// However, Spring requires that in order to instantiate the repository object in its context.
// That’s why we use a random entity just to extend the interface and make the autowiring work.
public interface SelfRegisteredRepository extends JpaRepository<RootEntity, Integer> {
    @Query(nativeQuery = true, value = "SELECT l1.runtime_id FROM labels l1 JOIN labels l2 ON l1.runtime_id = l2.runtime_id WHERE l1.runtime_id IS NOT NULL AND l1.runtime_id IN (SELECT id FROM tenant_runtimes WHERE tenant_id = uuid(?1) AND owner = true) AND l1.key = ?2 AND l1.value = to_jsonb(?3) AND l2.key = ?4 AND l2.value = to_jsonb(?5)")
    Set<String> findSelfRegisteredRuntimesByLabels(String providerTenantId, String selfRegKey, String selfRegValue, String regionKey, String regionValue);

    @Query(nativeQuery = true, value = "SELECT id FROM tenant_runtime_contexts trc WHERE trc.tenant_id = uuid(?1) AND trc.id IN (SELECT id FROM runtime_contexts WHERE runtime_id = uuid(?2)) AND trc.owner = true")
    String getRuntimeSubscriptionAvailableInTenant(String consumerTenantId, String runtimeId);

    @Query(nativeQuery = true, value = "SELECT f.id FROM formations f WHERE jsonb_exists((SELECT l.value FROM labels l WHERE l.key = 'scenarios' and l.runtime_context_id = uuid(?1)), f.name) AND f.formation_template_id IN (SELECT ft.id FROM formation_templates ft WHERE jsonb_exists(ft.discovery_consumers, (SELECT l.value->>0 FROM labels l WHERE l.key = 'runtimeType' AND l.runtime_id = (SELECT rtctx.runtime_id FROM runtime_contexts rtctx where rtctx.id = uuid(?1)))))")
    Set<String> getFormationsThatRuntimeSubscriptionAvailableInTenantIsPartOf(String rtCtxID);

    @Query(nativeQuery = true, value = "SELECT l1.app_template_id FROM labels l1 JOIN labels l2 ON l1.app_template_id = l2.app_template_id JOIN labels l3 ON l2.app_template_id = l3.app_template_id WHERE l1.app_template_id IS NOT NULL AND l1.key = ?2 AND l1.value = to_jsonb(?3) AND l2.key = ?4 AND l2.value = to_jsonb(?5) AND l3.key = 'global_subaccount_id' AND l3.value IN (SELECT to_jsonb(external_tenant) FROM business_tenant_mappings WHERE id = uuid(?1))")
    Set<String> findSelfRegisteredApplicationTemplatesByLabels(String providerTenantId, String selfRegKey, String selfRegValue, String regionKey, String regionValue);

    @Query(nativeQuery = true, value = "SELECT id FROM tenant_applications ta WHERE ta.tenant_id = uuid(?1) AND ta.id IN (SELECT id FROM applications WHERE app_template_id = uuid(?2)) AND ta.owner = true")
    String getApplicationSubscriptionAvailableInTenant(String consumerTenantId, String appTmplId);

    @Query(nativeQuery = true, value = "SELECT f.id FROM formations f WHERE jsonb_exists((SELECT l.value FROM labels l WHERE l.key = 'scenarios' and l.app_id = uuid(?1)), f.name) AND f.formation_template_id IN (SELECT ft.id FROM formation_templates ft WHERE jsonb_exists(ft.discovery_consumers, (SELECT l.value->>0 FROM labels l WHERE l.key = 'applicationType' AND l.app_id = uuid(?1))))")
    Set<String> getFormationsThatApplicationIsPartOf(String appId);

    @Query(nativeQuery = true, value = "SELECT id FROM applications WHERE local_tenant_id = ?1 and app_template_id = uuid(?2)")
    String findApplicationByLocalTenantIdAndApplicationTemplateId(String appLocalTenantId, String appTemplateId);

    @Query(nativeQuery = true, value = "SELECT id FROM business_tenant_mappings WHERE external_tenant = (SELECT value->>0 FROM labels where app_id = uuid(?1) AND key = ?2)")
    String findInternalApplicationTenantByLabelKey(String appId, String labelKey);
}