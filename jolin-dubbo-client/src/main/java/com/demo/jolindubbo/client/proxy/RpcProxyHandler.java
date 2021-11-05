package com.demo.jolindubbo.client.proxy;

import com.demo.jolindubbo.api.bean.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcProxyHandler extends SimpleChannelInboundHandler {
    private Object response;

    public Object getResponse() {
        return response;
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
//        System.out.println("Server: " + channelHandlerContext.channel().remoteAddress());
        this.response = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}