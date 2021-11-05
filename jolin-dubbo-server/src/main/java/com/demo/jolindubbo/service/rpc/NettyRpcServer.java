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

import java.util.HashMap;
import java.util.Map;

public class NettyRpcServer {

    private IRegisterCenter registerCenter;
    private String serviceAddress;
    private Map<String, Object> handlerMap = new HashMap<String, Object>(16);

    public NettyRpcServer(IRegisterCenter registerCenter, String serviceAddress) {
        this.registerCenter = registerCenter;
        this.serviceAddress = serviceAddress;
    }

    /**
     * 发布服务
     */
    public void publisher() {
        for (String serviceName : handlerMap.keySet()) {
            registerCenter.registry(serviceName, serviceAddress);
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //启动netty服务
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline channelPipeline = channel.pipeline();
                            channelPipeline.addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            channelPipeline.addLast(new ObjectEncoder());
                            channelPipeline.addLast(new RpcServerHandler(handlerMap));
                        }
                    });
            String[] addr = serviceAddress.split(":");
            String ip = addr[0];
            int port = Integer.valueOf(addr[1]);
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            System.out.println("服务启动，成功。");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 子对象的实现
     *
     * @param services 对象实现类
     */
    public void bind(Object... services) {
        //将实现类通过注解获取实现类的名称、实现类的实现放入map集合中。
        for (Object service : services) {
            RpcAnnotation annotation = service.getClass().getAnnotation(RpcAnnotation.class);
            String serviceName = annotation.clazz().getName();
            handlerMap.put(serviceName, service);
        }
    }
}