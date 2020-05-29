package com.montilla.laboratory.myfeatures.api.delegate;

import com.montilla.laboratory.myfeatures.api.model.HelloApiRequest;
import com.montilla.laboratory.myfeatures.api.model.HelloApiResponse;
import com.montilla.laboratory.myfeatures.common.iod.exception.TierException;

public interface IoDApiDelegate {

    default String sayHello(String name) {
        throw new TierException("Not Implemented Method [sayHello.IoDApiDelegate]");
    }

    default HelloApiResponse sayHelloWithFirstNameLastNameService(HelloApiRequest nameRequest, String lname) {
        throw new TierException("Not Implemented Method [sayHelloWithFnameLname.IoDApiDelegate]");
    }


}
