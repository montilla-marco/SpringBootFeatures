package com.montilla.laboratory.myfeatures.api.controller;

import com.montilla.laboratory.myfeatures.api.model.HelloApiRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface IoDApi {

    @RequestMapping(
            value = {"/iodhello"},
            produces = {"application/json"},
            method = {RequestMethod.GET}
    )
    ResponseEntity<String> sayHello(@RequestParam(value = "name", required = true) String name);

    @RequestMapping(
            value = {"/iodhello"},
            produces = {"application/json"},
            method = {RequestMethod.GET}
    )
    ResponseEntity<String> sayHelloWitHFNaneAndLastName(
            @RequestBody HelloApiRequest name),
            @RequestHeader String lastnamename);
    }
}
