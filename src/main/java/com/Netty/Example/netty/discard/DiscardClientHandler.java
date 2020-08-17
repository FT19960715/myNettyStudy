package com.Netty.Example.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelFutureListener;

/**
 * * Handles a client-side channel.
 */
public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {
    //  此接口提供了一个或多个原始字节数组byte[] 和ByteBuffer NIO缓冲区 的抽象视图
    private ByteBuf content;
    private ChannelHandlerContext ctx;

    /**
     * 调用ChannelHandlerContext.fireChannelActive() 以转发到 ChannelPipeline 中的下一个 ChannelInboundHandler
     * 子类可以重写此方法以更改行为
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        // 初始化消息
        content = ctx.alloc().directBuffer(DiscardClient.SIZE).writeZero(DiscardClient.SIZE);
        // 发送初始消息
        generateTraffic();
    }

    /**
     * 调用ChannelHandlerContext.fireChannelInactive() 以转发到ChannelPipeline 中的 ChannelInboundHandler
     * 子类可以重写此方法以更改行为
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        content.release();
    }

    /**
     * 请记住,此方法讲重命名为 messageReceived
     * 对于每个类型为L的消息，都被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 服务器应该什么也不发送，但是如果发送任何东西，则将其丢弃。
    }

    /**
     * 调用ChannelHandlerContext.fireExceptionCaught() 进行转发到ChannelPipeline 中的下一个ChannelHandler
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    long counter;

    private void generateTraffic() {
        // Flush the outbound buffer to the socket.
        // Once flushed, generate the same amount of traffic again.
        ctx.writeAndFlush(content.retainedDuplicate()).addListener(trafficGenerator);
    }

    private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) {
            if (future.isSuccess()) {
                generateTraffic();
            } else {
                future.cause().printStackTrace();
                future.channel().close();
            }
        }
    };
}