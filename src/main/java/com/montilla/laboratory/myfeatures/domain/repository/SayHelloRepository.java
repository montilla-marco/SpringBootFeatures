package com.montilla.laboratory.myfeatures.domain.repository;

import com.montilla.laboratory.myfeatures.common.iod.RepositoryCall;

public interface SayHelloRepository extends RepositoryCall<String, String> {

    @Override
    String call(String input);
}
