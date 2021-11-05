package com.demo.jolindubbo.service;

import com.demo.jolindubbo.api.Iservice.IService;
import com.demo.jolindubbo.service.registry.IRegisterCenter;
import com.demo.jolindubbo.service.registry.RegisterCenterImpl;
import com.demo.jolindubbo.service.rpc.DemoServer;
import com.demo.jolindubbo.service.rpc.NettyRpcServer;
import com.demo.jolindubbo.service.service.ServiceImpl;

public class DemoServerTest {
    public static void main(String[] args) {
        DemoServer demoServer = new DemoServer();
        demoServer.StartNetty();
    }
}