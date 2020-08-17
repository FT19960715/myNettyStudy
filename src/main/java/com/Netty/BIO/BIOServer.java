package com.Netty.BIO;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        // TODO: 2020-08-10 创建一个线程池
        // 如果有客户端链接，就创建一个线程，与之通讯

        ExecutorService service = Executors.newCachedThreadPool();  // 创建一个线程池
        // 创建一个serverSocket
        System.out.println("等待链接");
        ServerSocket socket = new ServerSocket(6666);
        System.out.println("服务器启动");
        while (true){
            // 监听，等待客户端链接
            Socket accept = socket.accept();
            System.out.println("有客户端链接");
            System.out.println(socket.getLocalPort());
            service.execute(()->{
                hanlder(accept);
            });
        }
    }

    // 编写handler 方法处理客户端请求
    public static void hanlder(Socket socket){
        System.out.println("线程id："+Thread.currentThread().getId() + "线程名称："+Thread.currentThread().getName());
        try {
            byte[] bytes = new byte[1024];
            //通过sockey获取输入流
            System.out.println("等待 read...");
            InputStream inputStream = socket.getInputStream();
//            循环读取客户端发送来的输入流
            while (true){
                int read = inputStream.read(bytes);
                if(read == -1){break;}
                else System.out.println(new String(bytes,0,read));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
