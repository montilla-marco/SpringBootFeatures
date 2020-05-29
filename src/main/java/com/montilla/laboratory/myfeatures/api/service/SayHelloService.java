package com.montilla.laboratory.myfeatures.api.service;

import com.montilla.laboratory.myfeatures.api.delegate.IoDApiDelegate;

public interface SayHelloService extends IoDApiDelegate {

    String sayHello(String name);
}
