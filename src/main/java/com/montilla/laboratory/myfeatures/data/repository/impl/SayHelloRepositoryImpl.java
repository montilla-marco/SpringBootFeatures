package com.montilla.laboratory.myfeatures.data.repository.impl;

import com.montilla.laboratory.myfeatures.common.iod.IoDConnectorTemplate;
import com.montilla.laboratory.myfeatures.common.iod.exception.handler.DataExceptionHandlerImpl;
import com.montilla.laboratory.myfeatures.data.datasource.SayHelloDataSource;
import com.montilla.laboratory.myfeatures.data.datasource.gateway.mapper.DataDomainMapper;
import com.montilla.laboratory.myfeatures.data.datasource.gateway.mapper.DomainDataMapper;
import com.montilla.laboratory.myfeatures.domain.repository.SayHelloRepository;

public class SayHelloRepositoryImpl extends IoDConnectorTemplate<String, String> implements SayHelloRepository  {

    public SayHelloRepositoryImpl(DomainDataMapper requestMapper,
                                  SayHelloDataSource abstractionCall,
                                  DataDomainMapper responseMapper,
                                  DataExceptionHandlerImpl exceptionHandler) {
        super(requestMapper, abstractionCall, responseMapper, exceptionHandler);
    }


    @Override
    public String call(String input) {
        return super.execute(input);
    }
}
