package com.montilla.laboratory.myfeatures.api.service.impl;

import com.montilla.laboratory.myfeatures.api.mapper.SayHelloApiDomainMapper;
import com.montilla.laboratory.myfeatures.api.mapper.SayHelloDomainApiMapper;
import com.montilla.laboratory.myfeatures.api.service.SayHelloService;
import com.montilla.laboratory.myfeatures.common.iod.AbstractionCall;
import com.montilla.laboratory.myfeatures.common.iod.IoDConnector;
import com.montilla.laboratory.myfeatures.common.iod.IoDConnectorTemplate;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.exception.handler.ApiExceptionHandlerImpl;
import com.montilla.laboratory.myfeatures.common.iod.mapping.TierMapper;
import com.montilla.laboratory.myfeatures.domain.usecase.SayHelloUseCase;

//@IoDConnector( requestMapper = SayHelloApiDomainMapper.class,
//        function = SayHelloUseCase.class,
//        responseMapper = SayHelloDomainApiMapper.clas,
//        exceptionHandler = ApiExceptionHandlerImpl.class,
//        IoCContatiner = SPRING)

public class SayHelloServiceImpl extends IoDConnectorTemplate<String, String> implements SayHelloService, IoDConnector {


    public SayHelloServiceImpl(TierMapper requestMapper,
                               AbstractionCall abstractionCall,
                               TierMapper responseMapper,
                               TierExceptionHandler exceptionHandler) {
        super(requestMapper, abstractionCall, responseMapper, exceptionHandler);
    }

    @Override
    public String sayHello(String name) {
        //Put before business logic
        return super.execute(name);
    }

    @Override
    public Object connect(Object upTierRequest) {
        return null;
    }
}
