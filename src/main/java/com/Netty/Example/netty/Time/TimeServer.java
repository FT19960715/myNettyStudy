package com.Netty.Example.netty.Time;

import com.Netty.Example.netty.Time.handle.TimeEncoder;
import com.Netty.Example.netty.Time.handle.TimeServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
    private int port;
    public TimeServer(int port){this.port = port;}

    public void run()throws  Exception{
        try {
            // NioEventLoopGroup是一个处理I / O操作的多线程事件循环 netty为不同类型的传输提供了各种EventLoopGroup实现
            // 使用NioEventLoopGroup。第一个通常称为“老板”，接受传入的连接。第二个通常称为“工人”，
            // 一旦老板接受连接并将注册的连接注册给工人，便处理已接受连接的流量。使用多少个线程以及如
            // 何将它们映射到创建的通道取决于EventLoopGroup实现，甚至可以通过构造函数进行配置。
            EventLoopGroup bossGroup = new NioEventLoopGroup(); //
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            // ServerBootstrap是设置服务器的帮助程序类。您可以直接使用频道设置服务器。但是，请注意，这是一个单调乏味的过程，在大多数情况下您无需这样做。
            ServerBootstrap b = new ServerBootstrap();
            // group() 为父接收者和子客户端设置 EventLoopGroup 这些EventLoopGroup 用来处理ServerChannel 和 Channel
            b.group(bossGroup, workerGroup)
                    // 用于创建Channel实现没有no0-args的构造函数，则可以使用this或channelFactory
                    .channel(NioServerSocketChannel.class) // (3)
                    // 设置channelHandler 该服务用于处理Channel的请求，返回 ServerBootStrap
                    .childHandler(new ChannelInitializer<SocketChannel>() { //
                        /**
                         * 一旦注册了Channel 就会调用此方法，方法返回后，该实例将从Channel的ChannelPipeline中删除
                         * @param ch
                         * @throws Exception
                         */
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
//                            F.writeAndFlush()
                            // pipeline() 返回分配的ChannelPipeline
                            // addLast() 在此管道的最后位置插入ChannelHandler，入参是一个可变数组，所以在这个地方可以传入多个ChannelHandler
                            ch.pipeline().addLast(new TimeServerHandler());
                        }
                    })
                    // 允许指定一个ChannelOption 该实例在创建后便用于Channel 实例。第一个参数使用null会抛出异常。第二个参数使用 null 删除先前设置的ChannelOption
                    .option(ChannelOption.SO_BACKLOG, 128)          //
                    // 允许指定一个{@link ChannelOption}，该实例一旦创建就用于{@link Channel}实例（在接受者接受{@link Channel}之后）。
                    // 使用值{@code null}删除先前设置的{@link ChannelOption}。
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //
            // bind() 创建一个新的{@link Channel}并将其绑定。
            ChannelFuture f = b.bind(port).sync(); //
            System.out.println("TimeServer启动");
            // 等待直到服务器插座关闭。
            // 在此示例中，这不会发生，但是您可以适当地做到这一点
            // 关闭服务
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    public static void main(String[] args) throws Exception {
        TimeServer timeServer = new TimeServer(8080);
        timeServer.run();
    }
}
