package com.demo.jolindubbo.service.rpc;

import com.demo.jolindubbo.api.bean.DemoBean;
import com.demo.jolindubbo.api.bean.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DemoHandler extends SimpleChannelInboundHandler<DemoBean> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DemoBean bean) throws Exception {

        System.out.println(bean.getText());

        ctx.writeAndFlush(bean);
    }

}