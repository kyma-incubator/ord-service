package com.sap.cloud.cmp.ord.service.repository;

import com.sap.cloud.cmp.ord.service.storage.model.SchemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface SchemaRepository extends JpaRepository<SchemaEntity, BigInteger> {
    SchemaEntity getByVersionNotNull();
}
