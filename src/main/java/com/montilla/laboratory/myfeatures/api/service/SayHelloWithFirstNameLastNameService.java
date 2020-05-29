package com.montilla.laboratory.myfeatures.api.service;

import com.montilla.laboratory.myfeatures.api.delegate.IoDApiDelegate;
import com.montilla.laboratory.myfeatures.api.model.HelloApiResponse;

public interface SayHelloWithFirstNameLastNameService extends IoDApiDelegate {

    HelloApiResponse sayHelloWithFirstNameLastNameService(String HelloApiRequest, String lname);
}
