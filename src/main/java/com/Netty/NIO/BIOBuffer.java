package com.Netty.NIO;

import java.nio.IntBuffer;

public class BIOBuffer {
    public static void main(String[] args) {
        // 举例说明Buffer的使用
        // 创建一个Buffer,大小为5，可以存放五个int
        IntBuffer buffer = IntBuffer.allocate(5);
        // 向Buffer中存放数据
//        buffer.put(10);
//        buffer.put(11);
//        buffer.put(12);
//        buffer.put(13);
//        buffer.put(14);
//        循环放入数据
        for(int i=0;i<buffer.capacity();i++){
            buffer.put(i*3);
        }
        // 循环读取数据
        // 将buffer转换,读写切换
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
