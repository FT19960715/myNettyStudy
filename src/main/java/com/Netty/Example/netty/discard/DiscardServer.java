package com.Netty.Example.netty.discard;
import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
public class DiscardServer {
    private int port;
    public DiscardServer(int port){this.port = port;}
    public void run()throws  Exception{
        try {
            // NioEventLoopGroup是一个处理I / O操作的多线程事件循环 netty为不同类型的传输提供了各种EventLoopGroup实现
            // 使用NioEventLoopGroup。第一个通常称为“老板”，接受传入的连接。第二个通常称为“工人”，
            // 一旦老板接受连接并将注册的连接注册给工人，便处理已接受连接的流量。使用多少个线程以及如
            // 何将它们映射到创建的通道取决于EventLoopGroup实现，甚至可以通过构造函数进行配置。
            EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            // ServerBootstrap是设置服务器的帮助程序类。您可以直接使用频道设置服务器。但是，请注意，这是一个单调乏味的过程，在大多数情况下您无需这样做。
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 用于创建Channel实现没有no0-args的构造函数，则可以使用this或channelFactory
                    .channel(NioServerSocketChannel.class) // (3)
                    // 设置channelHandler 该服务用于处理Channel的请求
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
            // 等待直到服务器插座关闭。
            // 在此示例中，这不会发生，但是您可以适当地做到这一点
            // 关闭服务
            f.channel().closeFuture().sync();
            //当写请求完成时，我们如何得到通知？这就像将ChannelFutureListener添加到返回的ChannelFuture一样简单。在这里，
            // 我们创建了一个新的匿名ChannelFutureListener，当操作完成时它将关闭Channel。 另外，您可以使用预定义的侦听器简化代码：
            f.addListener(ChannelFutureListener.CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    public static void main(String[] args)throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }
}
