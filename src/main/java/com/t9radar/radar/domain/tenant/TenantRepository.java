package com.t9radar.radar.domain.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>,
    JpaSpecificationExecutor<Tenant> {
}
