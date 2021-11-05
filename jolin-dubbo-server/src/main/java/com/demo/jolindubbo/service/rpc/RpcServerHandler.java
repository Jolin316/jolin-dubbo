package com.demo.jolindubbo.service.rpc;

import com.demo.jolindubbo.api.bean.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private Map<String, Object> handlerMap = new HashMap<String, Object>();

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channelActive:" + ctx.channel().remoteAddress());
        ctx.channel().read();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        System.out.println("服务端接收到消息：" + rpcRequest.toString());
        Object result = new Object();
        if (handlerMap.containsKey(rpcRequest.getClassName())) {
            Object clazz = handlerMap.get(rpcRequest.getClassName());
            Method method = clazz.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getTypes());
            result = method.invoke(clazz, rpcRequest.getParams());
        }
        ctx.writeAndFlush(result);
        ctx.close();
    }

    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest rpcRequest=(RpcRequest)msg;
            System.out.println("服务端接收到消息：" + rpcRequest.toString());
            Object result = new Object();
            if (handlerMap.containsKey(rpcRequest.getClassName())) {
                Object clazz = handlerMap.get(rpcRequest.getClassName());
                Method method = clazz.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getTypes());
                result = method.invoke(clazz, rpcRequest.getParams());
            }
            ctx.writeAndFlush(result);
            ctx.close();
        }
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}