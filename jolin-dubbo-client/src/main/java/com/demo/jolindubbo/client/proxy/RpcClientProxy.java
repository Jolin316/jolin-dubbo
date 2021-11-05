package com.demo.jolindubbo.client.proxy;

import com.demo.jolindubbo.api.bean.RpcRequest;
import com.demo.jolindubbo.client.registry.IServiceDiscover;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy {
    private static IServiceDiscover serviceDiscover;

    public RpcClientProxy(IServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    public static <T> T create(Class<?> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//						if (Object.class.equals(method.getDeclaringClass())) {
//							return method.invoke(this, args);
//						}
                        return rpcInvoke(clazz, method, args);
                    }

                });
    }


    private static Object rpcInvoke(Class<?> clazz, Method method, Object[] args) {

        //服务发现，zk进行通讯
        String serviceName = clazz.getName();
        //获取服务实现url地址
        String serviceAddress = serviceDiscover.discover(serviceName);
        //解析ip和port
        System.out.println("服务端实现地址：" + serviceAddress);
        String[] arrs = serviceAddress.split(":");
        String host = arrs[0];
        int port = Integer.parseInt(arrs[1]);
        System.out.println("服务实现ip:" + host);
        System.out.println("服务实现port:" + port);
        final RpcProxyHandler rpcProxyHandler = new RpcProxyHandler();
        //通过netty方式进行连接发送数据
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            channelPipeline.addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            channelPipeline.addLast(new ObjectEncoder());
                            channelPipeline.addLast(rpcProxyHandler);
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();

            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setClassName(method.getDeclaringClass().getName());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setTypes(method.getParameterTypes());
            rpcRequest.setParams(args);

            future.channel().writeAndFlush(rpcRequest);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return rpcProxyHandler.getResponse();
    }

}