package com.montilla.laboratory.myfeatures.data.datasource.gateway;

import com.montilla.laboratory.myfeatures.data.datasource.SayHelloDataSource;

public class SayHelloDataSourceImpl implements SayHelloDataSource {

    @Override
    public String apply(String input) {
        return "Hello from  Data Tier " + input + "!!!";
    }
}
