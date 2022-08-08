create or replace view tenants_destinations(tenant_id, id, name, type, url, authentication, bundle_id, revision) as
SELECT DISTINCT dst.tenant_id,
                dests.id,
                dests.name,
                dests.type,
                dests.url,
                dests.authentication,
                dests.bundle_id,
                dests.revision
FROM destinations dests
         JOIN (SELECT d.id,
                      d.tenant_id::text AS tenant_id
               FROM destinations d
               UNION ALL
               SELECT apps_subaccounts_func.id,
                      apps_subaccounts_func.tenant_id
               FROM apps_subaccounts_func() apps_subaccounts_func(id, tenant_id)) dst ON dests.id = dst.id;

drop view tenants_destinations

create or replace view tenants_destinations_sensitive_data(id, destination_id, sensitive_data) as
SELECT DISTINCT dests.id,
                dests.id,
                '__sensitive_data__' || dests.name || '__sensitive_data__'
FROM tenants_destinations dests

drop view tenants_destinations_sensitive_data

