package com.montilla.laboratory.myfeatures.api.controller.impl;

import com.montilla.laboratory.myfeatures.api.controller.IoDApi;
import com.montilla.laboratory.myfeatures.api.delegate.IoDApiDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IoDApiController implements IoDApi {

    private IoDApiDelegate apiDelegate;

    public IoDApiController(IoDApiDelegate apiDelegate) {
        this.apiDelegate = apiDelegate;
    }

    @Override
    public ResponseEntity<String> sayHello(String name) {
        String response = apiDelegate.sayHello(name);
        return  ResponseEntity.ok(response);
    }
}
