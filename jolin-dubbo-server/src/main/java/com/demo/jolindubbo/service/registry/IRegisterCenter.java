package com.demo.jolindubbo.service.registry;

public interface IRegisterCenter {
    /**
     * 服务注册
     * @param serverName 服务名称（实现方法路径）
     * @param serviceAddress 服务地址
     */
    void registry(String serverName, String serviceAddress);
}