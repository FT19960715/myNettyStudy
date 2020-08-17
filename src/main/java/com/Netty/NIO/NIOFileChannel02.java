package com.Netty.NIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) {
        File file = new File("1.txt");
        try {
            FileInputStream inputStream = new FileInputStream(file);
            FileChannel channel = inputStream.getChannel();
            FileOutputStream outputStream = new FileOutputStream("2.txt");
            FileChannel channel1 = outputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (true) {
                byteBuffer.clear(); // 复位数据
                int read = channel.read(byteBuffer);
                if (read != -1) {
                    byteBuffer.flip();
                    channel1.write(byteBuffer);
                } else {
                    break;
                }
            }
            channel.close();
            channel1.close();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
