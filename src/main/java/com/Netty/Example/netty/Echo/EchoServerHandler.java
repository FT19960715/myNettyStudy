package com.Netty.Example.netty.Echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    // 我们在这里重写channelRead（）事件处理程序方法。每当从客户端接收到新数据时，就会使用接收到的消息来调用此方法。
    // 在此实例中使用echo任何发出的消息都会被发回
    // ChannelHandlerContext对象提供了各种操作，使您能够触发各种I/O事件和操作。
    // 在这里我们不需要释放掉收到的消息{Object msg}，因为netty在将其写道网络时会为您释放它
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }
}
