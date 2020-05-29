package com.montilla.laboratory.myfeatures.domain.usecase.impl;

import com.montilla.laboratory.myfeatures.common.iod.AbstractionCall;
import com.montilla.laboratory.myfeatures.common.iod.AbstractionTemplate;
import com.montilla.laboratory.myfeatures.common.iod.exception.handler.DomainExceptionHandlerImpl;
import com.montilla.laboratory.myfeatures.domain.repository.SayHelloRepository;
import com.montilla.laboratory.myfeatures.domain.usecase.SayHelloUseCase;


public class SayHelloUseCaseImpl extends AbstractionTemplate<String, String>
                        implements SayHelloUseCase, AbstractionCall<String, String> {


    public SayHelloUseCaseImpl(SayHelloRepository repositoryCall,
                               DomainExceptionHandlerImpl exceptionHandler) {
        super(repositoryCall, exceptionHandler);
    }

    @Override
    public String sayHelloRepository(String name) {
        return this.apply(name);
    }

    @Override
    public String apply(String input) {
        return super.repoConnect(input);
    }
}
