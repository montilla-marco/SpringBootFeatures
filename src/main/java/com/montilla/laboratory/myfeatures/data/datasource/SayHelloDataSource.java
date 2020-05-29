package com.montilla.laboratory.myfeatures.data.datasource;

import com.montilla.laboratory.myfeatures.common.iod.AbstractionCall;

public interface SayHelloDataSource extends AbstractionCall<String, String> {

    @Override
    String apply(String input);
}
