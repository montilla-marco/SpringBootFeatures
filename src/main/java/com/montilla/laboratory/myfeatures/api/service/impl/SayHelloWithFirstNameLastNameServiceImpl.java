package com.montilla.laboratory.myfeatures.api.service.impl;

import com.montilla.laboratory.myfeatures.api.model.HelloApiRequest;
import com.montilla.laboratory.myfeatures.api.model.HelloApiResponse;
import com.montilla.laboratory.myfeatures.api.service.SayHelloWithFirstNameLastNameService;
import com.montilla.laboratory.myfeatures.common.iod.AbstractionCall;
import com.montilla.laboratory.myfeatures.common.iod.IoDConnectorTemplate;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierExceptionHandler;
import com.montilla.laboratory.myfeatures.common.iod.mapping.TierMapper;

public class SayHelloWithFirstNameLastNameServiceImpl extends IoDConnectorTemplate<HelloApiRequest, HelloApiResponse> implements SayHelloWithFirstNameLastNameService {

    public SayHelloWithFirstNameLastNameServiceImpl(TierMapper requestMapper,
                                                    AbstractionCall abstractionCall,
                                                    TierMapper responseMapper,
                                                    TierExceptionHandler exceptionHandler) {
        super(requestMapper, abstractionCall, responseMapper, exceptionHandler);
    }

    @Override
    public HelloApiResponse sayHelloWithFirstNameLastNameService(String HelloApiRequest, String lname) {
        return null;
    }
}
