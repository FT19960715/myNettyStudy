package com.Netty.NIO;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 说明：MappedByteBuffer 可以让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次文件
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
        // 获取对应文件通道
        FileChannel channel = randomAccessFile.getChannel();
        // 参数1 使用独写模式 参数 2 可以直接修改的起始位置 ，参数3 表示内存中的大小，5表示最多可以映射五个字节
        // 即 将文件的多少个字节映射到内存，可以直接修改的范围是 0-5；
        channel.size();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0,(byte)'H');
        map.put(3,(byte)'9');
        randomAccessFile.close();
    }
}
