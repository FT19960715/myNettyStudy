package com.Netty.Example.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/***
 * 实现DISCARD协议，忽略所有接收到的数据。让我们直接从处理程序实现开始，该实现处理Netty生成的I / O事件
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 我们在这里重写channelRead（）事件处理程序方法。每当从客户端接收到新数据时，就会使用接收到的消息来调用此方法。在此示例中，接收到的消息的类型为ByteBuf。
     * 为了实现DISCARD协议，处理程序必须忽略收到的消息。 ByteBuf是一个引用计数的对象，必须通过release（）方法显式释放它。请记住，释放任何传递给处理程序的引用计数对象是处理程序的责任。
     * 通常，channelRead（）处理程序方法的实现方式如下：
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 静默丢弃收到的数据
//        ((ByteBuf) msg).release(); // (3)
//        try {
//            // Do something with msg
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
        ByteBuf in = (ByteBuf) msg;
        try {
            System.out.println(in.toString(CharsetUtil.US_ASCII));
//            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
        } finally {
            ReferenceCountUtil.release(msg); // (2)
        }
    }

    /***
     * 当Netty由于I / O错误而引发异常时，或者由于处理事件时引发异常而由处理程序实现引发异常时，将使用Throwable调用exceptionCaught（）事件处理程序方法。
     * 在大多数情况下，应该记录捕获的异常并在此处关闭其关联的通道，尽管此方法的实现可能会有所不同，
     * 具体取决于您要处理特殊情况时要采取的措施。例如，您可能想在关闭连接之前发送带有错误代码的响应消息
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 引发异常时关闭链接
        cause.printStackTrace();
        ctx.close();
    }
}
