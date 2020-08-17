package com.Netty.Example.netty.Time;

import com.Netty.Example.netty.Time.handle.TimeClientHandler;
import com.Netty.Example.netty.Time.handle.TimeDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class TimeClient {
    public static void main(String[] args) {
//        String host = args[0] == null ?"localhost":args[0];
        String host = "localhost";
//        int port = Integer.parseInt(args[1]);
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            // Bootstrap与ServerBootstrap相似，除了它用于非服务器Channel，例如客户端或无连接Channel
            Bootstrap b = new Bootstrap();
            //如果仅指定一个EventLoopGroup，则它将同时用作老板组和工作组。但是，老板工人并不用于客户端
            b.group(workerGroup);
//            NioSocketChannel被用来创建客户端通道。
            b.channel(NioSocketChannel.class);
//            请注意，由于客户端SocketChannel没有父级，因此此处不像使用ServerBootstrap那样使用childOption（）
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeDecoder(),new TimeClientHandler());
                }
            });
            // 建立链接
            System.out.println("建立链接");
            ChannelFuture future = b.connect(host, port).sync();
            future.channel().closeFuture().sync();
            // TODO: 2020-08-10 添加监听,当ChannelFuture完成之后的回调 当写请求完成时，我们如何得到通知？这就像将ChannelFutureListener添加到返回的ChannelFuture一样简单。在这里，我们创建了一个新的匿名ChannelFutureListener，当操作完成时它将关闭Channel。 另外，您可以使用预定义的侦听器简化代码：
//            future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
