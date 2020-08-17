package com.Netty.NIO.NioServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞模式
        socketChannel.configureBlocking(false);
        // t提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);
        if(!socketChannel.connect(inetSocketAddress)){
            // 链接到服务器成功
            while (!socketChannel.finishConnect()){
                System.out.println("因为链接需要事件，客户端不会阻塞，可以做其他工作");
            }
        }
            System.out.println(123);
            // 如果链接成功，就发送数据
            String str = "hello world";
            // 包裹字节数组到buffer中去
            ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
            // 发送数据，将buffer中的数据写入到channel中去
            socketChannel.write(buffer);
            System.in.read();
    }
}
