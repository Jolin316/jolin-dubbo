package com.demo.jolindubbo.service.rpc;

import com.demo.jolindubbo.common.RpcAnnotation;
import com.demo.jolindubbo.service.registry.IRegisterCenter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashMap;
import java.util.Map;

public class DemoServer {

    public void StartNetty(){

        EventLoopGroup acceptor = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptor,worker);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                            ClassResolvers.cacheDisabled(null)));
//                    ch.pipeline().addLast("decoder",new StringDecoder());
//                    ch.pipeline().addLast("encoder",new StringEncoder());
                    ch.pipeline().addLast(new DemoHandler());
                }
            });

            ChannelFuture f = bootstrap.bind(8081).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            acceptor.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

}