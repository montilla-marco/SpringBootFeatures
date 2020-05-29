package com.montilla.laboratory.myfeatures.api.delegate.impl;

import com.montilla.laboratory.myfeatures.api.delegate.IoDApiDelegate;
import com.montilla.laboratory.myfeatures.api.service.SayHelloService;

public class IoDApiDelegateImpl implements IoDApiDelegate {

    private SayHelloService sayHelloService;

    public IoDApiDelegateImpl(SayHelloService sayHelloService) {
        this.sayHelloService = sayHelloService;
    }

    @Override
    public String sayHello(String name) {
        return sayHelloService.sayHello(name);
    }
}
