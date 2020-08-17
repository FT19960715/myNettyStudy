package com.Netty.Example.netty.Time.handle;

import com.Netty.Example.netty.Time.entity.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    protected void ensureNotSharable() {
        System.out.println("ensureNotSharable 执行");
        System.out.println(System.currentTimeMillis());
        super.ensureNotSharable();
    }

    @Override
    public boolean isSharable() {
        System.out.println("isSharable 执行");
        System.out.println(System.currentTimeMillis());
        return super.isSharable();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded 执行");
        System.out.println(System.currentTimeMillis());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 执行");
        System.out.println(System.currentTimeMillis());
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered 执行");
        System.out.println(System.currentTimeMillis());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered 执行");
        System.out.println(System.currentTimeMillis());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive 执行");
        System.out.println(System.currentTimeMillis());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive 执行");
        System.out.println(System.currentTimeMillis());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("TimeClient channelRead执行");
//        ByteBuf m = (ByteBuf) msg; // (1)
//        try {
//            System.out.println("服务端传过来的消息:"+msg);
//            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println("客户端输出");
//            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
//        } finally {
//            m.release();
//        }
        UnixTime m = (UnixTime) msg;
        System.out.println("m TimeCLientHandler 执行");
        System.out.println(m);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete 执行");
        System.out.println(System.currentTimeMillis());
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("userEventTriggered 执行");
        System.out.println(System.currentTimeMillis());
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelWritabilityChanged 执行");
        System.out.println(System.currentTimeMillis());
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("exceptionCaught 执行");
        System.out.println(System.currentTimeMillis());
        cause.printStackTrace();
        ctx.close();
    }
}