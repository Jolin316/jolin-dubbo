package com.demo.jolindubbo.client;

import com.demo.jolindubbo.api.Iservice.IService;
import com.demo.jolindubbo.client.proxy.RpcClientProxy;
import com.demo.jolindubbo.client.registry.IServiceDiscover;
import com.demo.jolindubbo.client.registry.ServiceDiscoverImpl;

import java.io.IOException;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        IServiceDiscover serviceDiscover = new ServiceDiscoverImpl();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscover);
        IService iService = rpcClientProxy.create(IService.class);
        String s1 = iService.sayHello("laohan1");
        System.out.println(s1);
        String s2 = iService.sayHello("laohan2");
        System.out.println(s2);
        String s3 = iService.sayHello("laohan3");
        System.out.println(s3);
        System.out.println(iService.add(10, 4));
    }
}