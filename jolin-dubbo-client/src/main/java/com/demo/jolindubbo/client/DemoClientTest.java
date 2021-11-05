package com.demo.jolindubbo.client;

import com.demo.jolindubbo.api.Iservice.IService;
import com.demo.jolindubbo.client.proxy.DemoProxy;
import com.demo.jolindubbo.client.proxy.RpcClientProxy;
import com.demo.jolindubbo.client.registry.IServiceDiscover;
import com.demo.jolindubbo.client.registry.ServiceDiscoverImpl;

import java.io.IOException;

public class DemoClientTest {
    public static void main(String[] args) throws Exception {
        DemoProxy demoProxy = new DemoProxy();
        demoProxy.connect();

    }
}