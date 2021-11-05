package com.demo.jolindubbo.client.proxy;

import com.demo.jolindubbo.api.bean.DemoBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class Demo2Handler extends SimpleChannelInboundHandler<DemoBean> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DemoBean bean) throws Exception {
        System.out.println(bean.getText());
    }
}