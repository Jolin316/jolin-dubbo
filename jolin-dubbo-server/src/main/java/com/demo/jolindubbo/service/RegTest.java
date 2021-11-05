package com.demo.jolindubbo.service;

import com.demo.jolindubbo.service.registry.IRegisterCenter;
import com.demo.jolindubbo.service.registry.RegisterCenterImpl;

import java.io.IOException;

public class RegTest {
    public static void main(String[] args) throws IOException {
        IRegisterCenter registerCenter = new RegisterCenterImpl();
        registerCenter.registry("com.demo.jolinhahahah.test", "127.0.0.1:9090");
        System.in.read();
    }
}