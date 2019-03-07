package com.lovesickness.o2o.other;

public class OperationProxy implements Operate {
    private Operator operator = null;

    @Override
    public void doSomething() {
        beforeDoSomething();
        if (operator == null) {
            operator = new Operator();
        }
        operator.doSomething();
        afterDoSomething();


    }

    private void beforeDoSomething() {
        System.out.println("before doing something");
    }

    private void afterDoSomething() {
        System.out.println("after doing something");
    }

}
