package com.montilla.laboratory.myfeatures.api.mapper;


import com.montilla.laboratory.myfeatures.common.iod.mapping.TierMapper;

public interface SayHelloApiDomainMapper extends TierMapper<String, String> {

    @Override
    String map(String input);
}
