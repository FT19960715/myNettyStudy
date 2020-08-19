package com.Netty.NIO.groupChat;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class GroupServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int port = 6667;
    // 构造器
    public GroupServer(){
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.bind(new InetSocketAddress(port));
            // 设置非阻塞
            listenChannel.configureBlocking(false);
            // 将该listen注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 监听
    public void listen(){
        try {
            while (true){
                int count = selector.select(2000);
                if(count > 0){
                    // 存在事件需要处理
                    // 遍历selectionKey 集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        // 监听到 accept 进行 accept
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.register(selector,SelectionKey.OP_READ);
                            // 给出提示，某某某上线了
                            System.out.println(sc.getRemoteAddress()+"上线了");
                        }
                        if(key.isReadable()){
                        }
                        // 删除当前key，防止重复处理
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待ing ...");
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
