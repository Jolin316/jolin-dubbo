package com.demo.jolindubbo.client.proxy;

import com.demo.jolindubbo.api.bean.DemoBean;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class DemoProxy {

    public void connect() throws Exception {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(worker);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                            ClassResolvers.cacheDisabled(null)));
//                    ch.pipeline().addLast("decoder",new StringDecoder());
//                    ch.pipeline().addLast("encoder",new StringEncoder());
                    ch.pipeline().addLast(new Demo2Handler());
                }
            });
            ChannelFuture f = b.connect("127.0.0.1", 8081).sync();

            Channel channel = f.channel();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String s = scanner.nextLine();
                DemoBean demoBean = new DemoBean();
                demoBean.setText(s);
                channel.writeAndFlush(demoBean);
            }

            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }

}