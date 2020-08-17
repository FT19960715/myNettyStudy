package com.Netty.NIO;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {
     String str = "hello word";
     // 创建一个输出流
        File file = new File("F:\\study\\01.txt");
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel channel = inputStream.getChannel();
//        创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int)file.length());
//        channel数据读入缓冲区
        int read = channel.read(buffer);
        System.out.println(new String(buffer.array()));
        inputStream.close();
    }
}
