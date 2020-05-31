package com.app.config.tenant.master.repository;

import com.app.config.tenant.master.entity.MasterTenant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterTenantRepository extends CrudRepository<MasterTenant, Long> { }
