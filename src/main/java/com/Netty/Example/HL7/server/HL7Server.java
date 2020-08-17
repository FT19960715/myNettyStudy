package com.Netty.Example.HL7.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HL7Server {
    int port;
    String ip;
    public HL7Server(int port,String ip){this.port = port;this.ip = ip;}
    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();// 1
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//2
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)//3
                    .childHandler(new ChannelInitializer<SocketChannel>() {//4
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)//5
                    .childOption(ChannelOption.SO_KEEPALIVE, true);//6
            // 启动
            ChannelFuture f = b.bind(ip,port).sync();//7
            System.out.println("服务启动成功");
            // 一直待机知道 server socket 关闭
            // 例子中没有关闭的情况，不过你可以有好的关闭服务器
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        new HL7Server(8099,"127.0.0.1").run();
    }
}
