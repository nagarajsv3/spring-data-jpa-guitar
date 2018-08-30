package com.nsv.guitarapp.repository;

public class ModelJpaRepositoryImpl implements ModelJpaRepositoryCustom {
    @Override
    public void aCustomMethod() {
        System.out.println("I am inside a Custom method implementation");
    }
}
