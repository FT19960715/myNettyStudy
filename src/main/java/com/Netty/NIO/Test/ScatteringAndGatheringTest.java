package com.Netty.NIO.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7555);
        // 绑定socket 并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        ByteBuffer[] byteBuffer = new ByteBuffer[2];
        byteBuffer[0] = ByteBuffer.allocate(5);
        byteBuffer[1] = ByteBuffer.allocate(3);
        SocketChannel socketChannel = serverSocketChannel.accept(); // 获取到socketChannel
        int messageLength = 8;  // 从客户端接受八个字节
        // 循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long read = socketChannel.read(byteBuffer);
                byteRead += read;
                System.out.println("byteRead=" + byteRead);
//                使用流打印，查看buffer 的position和 limit
                Arrays.asList(byteBuffer).stream().map(buffer -> "position=" + buffer.position() + "limit=" + buffer.limit()).forEach(System.out::println);
            }
            Arrays.asList(byteBuffer).forEach(buffer -> buffer.flip());

            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffer);
                byteWrite += 1;
            }
            Arrays.asList(byteBuffer).forEach(buffer -> buffer.clear());
            System.out.println("byteRead=" + byteRead + " byteWrite" + byteWrite + " MessageLength:" + messageLength);
            // telnet 127.0.0.1 7555   ctrl+]
        }
    }
}
