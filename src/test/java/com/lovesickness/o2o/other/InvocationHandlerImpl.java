package com.lovesickness.o2o.other;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvocationHandlerImpl implements InvocationHandler {
    private Operate operate;
    public InvocationHandlerImpl(Operate operate){
        this.operate = operate;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before calling method: " + method.getName());
        //调用操纵者的具体操作方法
        method.invoke(operate, args);
        System.out.println("after calling method: " + method.getName());
        return null;
    }
}
