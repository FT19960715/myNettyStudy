package com.Netty.NIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileCopyChannle {
    public static void main(String[] args) throws Exception {
        FileInputStream in = new FileInputStream("F:\\a.jpg");
        FileOutputStream out = new FileOutputStream("F:\\b.jpg");
        // 获取各个流对应的fileChannel
        FileChannel source = in.getChannel();
        FileChannel destCh = out.getChannel();
        // transferFrom完成copy
        destCh.transferFrom(source,0,source.size());
        source.close();
        destCh.close();
        in.close();out.close();

    }
}
