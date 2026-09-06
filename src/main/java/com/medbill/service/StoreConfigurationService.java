package com.medbill.service;

import com.medbill.entity.StoreConfiguration;

public interface StoreConfigurationService {

    StoreConfiguration getConfiguration(Long companyId);

    StoreConfiguration saveOrUpdateConfiguration(StoreConfiguration config, Long companyId);
}

