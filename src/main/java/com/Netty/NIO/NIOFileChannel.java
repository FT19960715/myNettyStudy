package com.Netty.NIO;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel {
    public static void main(String[] args) throws Exception {
     String str = "hello word";
     // 创建一个输出流
        File file = new File("F:\\study\\01.txt");
        if(!file.exists()){file.createNewFile();}
        FileOutputStream outputStream = new FileOutputStream(file);
        // FileChannel是一个抽象类,具体实现是FIleChannelImpl
        FileChannel channel = outputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        // 将输出输出到缓冲区中去
        allocate.put(str.getBytes());
        allocate.flip(); //反转
        // 将buffer数据写入到channel
        channel.write(allocate);
        //关闭流
        outputStream.close();
    }
}
