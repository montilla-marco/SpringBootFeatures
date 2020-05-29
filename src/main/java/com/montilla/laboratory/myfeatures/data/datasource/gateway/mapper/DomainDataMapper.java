package com.montilla.laboratory.myfeatures.data.datasource.gateway.mapper;

import com.montilla.laboratory.myfeatures.common.iod.mapping.TierMapper;

public interface DomainDataMapper extends TierMapper<String, String> {
    @Override
    String map(String input);
}
