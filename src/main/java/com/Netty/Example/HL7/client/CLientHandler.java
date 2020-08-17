package com.Netty.Example.HL7.client;

import com.Netty.Example.HL7.entity.Person;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CLientHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf byteBuf;

    /**
     * 接收到服务端消息的时候执行
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead(ChannelHandlerContext ctx, Object msg)");
        System.out.println("channelRead 执行");
        ByteBuf result = (ByteBuf)msg;
        byte[] result1 = new byte[result.readableBytes()];
        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
        result.readBytes(result1);
        String resultStr = new String(result1);
        // 接收并打印客户端的信息
        System.out.println("Server said:" + resultStr);
        result.release();// 释放资源
        ctx.close();    // 客户端接收到回执确认消息会就关闭掉
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete(ChannelHandlerContext ctx);");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("userEventTriggered(ChannelHandlerContext ctx, Object evt)");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelWritabilityChanged(ChannelHandlerContext ctx)");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught(ChannelHandlerContext ctx, Throwable cause)");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered(ChannelHandlerContext ctx)");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered(ChannelHandlerContext ctx)");
        super.channelUnregistered(ctx);
    }

    /**
     * client 启动的时候
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive(ChannelHandlerContext ctx)");
        System.out.println("channelActive 执行 建立链接");
        String personMsg =  new Person("张三","123").toString();
        ByteBuf buffer = ctx.alloc().buffer(4*personMsg.length());
        System.out.println("channelRead:people:"+personMsg);
        buffer.writeBytes(personMsg.getBytes());
        ctx.write(buffer);
        ctx.flush();
//        Thread.sleep(3000);
//        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive(ChannelHandlerContext ctx)");
        super.channelInactive(ctx);
    }
}
