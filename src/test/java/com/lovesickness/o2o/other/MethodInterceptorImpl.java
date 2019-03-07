package com.lovesickness.o2o.other;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MethodInterceptorImpl implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before calling method:" + method.getName());
        methodProxy.invokeSuper(o, objects);
        System.out.println("after calling method:" + method.getName());
        return null;

    }
}
