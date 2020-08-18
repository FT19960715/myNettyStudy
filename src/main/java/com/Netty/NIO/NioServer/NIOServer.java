package com.Netty.NIO.NioServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 得到一个Selector对象
        Selector selector = Selector.open();
        // 绑定一个端口
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1",6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 将ServerSocktChannel 注册打Selector， 关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 循环等待客户端链接
        while(true){
            // 这里阻塞五秒钟，如果五秒中无时间发生
            if(selector.select(1000) == 0){
                System.out.println("z暂时无事件发生");
                // 暂时没有任何事件发生
            }else{
                // 如果返回大于零,获取到相关的selectKey集合，有事件发生的selectKey
                // 通过selectKey可以获取到相关的SocketChannel
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey e = iterator.next();
                    // 更具通道发生的事件做对应的处理
                    if(e.isAcceptable()){
                        // 表示有客户端来链接，生成SocketChannel
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            // 将产生的socketChannel指定为非阻塞模型
                            socketChannel.configureBlocking(false);
                            // 将当前的socketChannel也注册到Selector上,关注事件为OP_READ，同时为该socketChannel关联一个Buffer
                            socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }else if(e.isReadable()){
                        // 从客户端读取数据，发生了 OP_READ
                        // 通过key反向获取到对应的Channel
                        SocketChannel channel =(SocketChannel) e.channel();
                        ByteBuffer buffer = (ByteBuffer)e.attachment();
                        // 读取buffer中的数据
                            channel.read(buffer);
                        System.out.println("客户端消息："+new String(buffer.array()));
                    }
                    // 手动从集合中移除selectKey，目的是防止重复操作
                }
//                selectionKeys.forEach(e->{
//                    // 更具通道发生的事件做对应的处理
//                    if(e.isAcceptable()){
//                        // 表示有客户端来链接，生成SocketChannel
//                        try {
//                            SocketChannel socketChannel = serverSocketChannel.accept();
//                            // 将当前的socketChannel也注册到Selector上,关注事件为OP_READ，同时为该socketChannel关联一个Buffer
//                            socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                    }else if(e.isReadable()){
//                        // 从客户端读取数据，发生了 OP_READ
//                        // 通过key反向获取到对应的Channel
//                        SocketChannel channel =(SocketChannel) e.channel();
//                        ByteBuffer buffer = (ByteBuffer)e.attachment();
//                        // 读取buffer中的数据
//                        try {
//                            int read = channel.read(buffer);
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                    // 手动从集合中移除selectKey，目的是防止重复操作
//                });
            }
        }
    }
}
