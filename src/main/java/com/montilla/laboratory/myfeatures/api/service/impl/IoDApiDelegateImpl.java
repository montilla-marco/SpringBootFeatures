package com.montilla.laboratory.myfeatures.api.service.impl;

import com.montilla.laboratory.myfeatures.api.delegate.IoDApiDelegate;
import com.montilla.laboratory.myfeatures.api.model.HelloApiRequest;
import com.montilla.laboratory.myfeatures.api.model.HelloApiResponse;

public class IoDApiDelegateImpl implements IoDApiDelegate {
    @Override
    public String sayHello(String name) {
        return null;
    }

    @Override
    public HelloApiResponse sayHelloWithFirstNameLastNameService(HelloApiRequest nameRequest, String lname) {
        return null;
    }
}
