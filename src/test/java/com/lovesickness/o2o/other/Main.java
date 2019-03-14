package com.lovesickness.o2o.other;

public class Main {
    static final int MAXIMUM_CAPACITY = 1 << 30;


    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {
//        Operate operate = new OperationProxy();
//        operate.doSomething();
        /**
         * JDK动态代理

         //实例化操作者
         Operate operate = new Operator();
         //将操作者对象进行注入
         InvocationHandlerImpl handler = new InvocationHandlerImpl(operate);
         //生成代理对象
         Operate operationProxy = (Operate) Proxy.newProxyInstance(operate.getClass().getClassLoader(),
         operate.getClass().getInterfaces(), handler);
         //调用操作方法
         operationProxy.doSomething();     **/
        /**
         * cglib动态代理

         Operator operator = new Operator();
         MethodInterceptorImpl methodInterceptorImpl = new MethodInterceptorImpl();

         //初始化加强器对象
         Enhancer enhancer = new Enhancer();
         //设置代理类
         enhancer.setSuperclass(operator.getClass());
         //设置代理回调
         enhancer.setCallback(methodInterceptorImpl);

         //创建代理对象
         Operator operationProxy = (Operator) enhancer.create();
         //调用操作方法
         operationProxy.doSomething();**/

//        System.out.println(new MyString().getStr());
//        Map map = new HashMap();
//        map.put("Str",new MyString().getStr());
//        System.out.println(tableSizeFor(30));;


    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}

class MyString {
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}

