package com.demo.jolindubbo.service;

import com.demo.jolindubbo.api.Iservice.IService;
import com.demo.jolindubbo.service.registry.IRegisterCenter;
import com.demo.jolindubbo.service.registry.RegisterCenterImpl;
import com.demo.jolindubbo.service.rpc.NettyRpcServer;
import com.demo.jolindubbo.service.service.ServiceImpl;

public class ServerTest {
    public static void main(String[] args) {
        IService service = new ServiceImpl();
        IRegisterCenter registerCenter = new RegisterCenterImpl();
        NettyRpcServer rpcServer = new NettyRpcServer(registerCenter, "127.0.0.1:8080");
        rpcServer.bind(service);
        rpcServer.publisher();
    }
}