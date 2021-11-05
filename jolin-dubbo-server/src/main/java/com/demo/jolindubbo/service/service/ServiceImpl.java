package com.demo.jolindubbo.service.service;

import com.demo.jolindubbo.api.Iservice.IService;
import com.demo.jolindubbo.common.RpcAnnotation;

@RpcAnnotation(clazz = IService.class)
public class ServiceImpl implements IService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
    @Override
    public String sayHello(String msg) {
        System.out.println("rpc say :" + msg);
        return "rpc say: " + msg;
    }
}
