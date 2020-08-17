package com.Netty.Example.netty.Time.handle;

import com.Netty.Example.netty.Time.entity.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TimeDecoder extends ByteToMessageDecoder {
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
    public void setSingleDecode(boolean singleDecode) {
        System.out.println("setSingleDecode 执行");
        System.out.println(System.currentTimeMillis());
        super.setSingleDecode(singleDecode);
    }

    @Override
    public boolean isSingleDecode() {
        System.out.println("isSingleDecode 执行");
        System.out.println(System.currentTimeMillis());
        return super.isSingleDecode();
    }

    @Override
    public void setCumulator(Cumulator cumulator) {
        System.out.println("setCumulator 执行");
        System.out.println(System.currentTimeMillis());
        super.setCumulator(cumulator);
    }

    @Override
    public void setDiscardAfterReads(int discardAfterReads) {
        System.out.println("setDiscardAfterReads 执行");
        System.out.println(System.currentTimeMillis());
        super.setDiscardAfterReads(discardAfterReads);
    }

    @Override
    protected int actualReadableBytes() {
        System.out.println("actualReadableBytes 执行");
        System.out.println(System.currentTimeMillis());
        return super.actualReadableBytes();
    }

    @Override
    protected ByteBuf internalBuffer() {
        System.out.println("internalBuffer 执行");
        System.out.println(System.currentTimeMillis());
        return super.internalBuffer();
    }

    @Override
    protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved0 执行");
        System.out.println(System.currentTimeMillis());
        super.handlerRemoved0(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 执行");
        System.out.println(System.currentTimeMillis());
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete 执行");
        System.out.println(System.currentTimeMillis());
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive 执行");
        System.out.println(System.currentTimeMillis());
        super.channelInactive(ctx);
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught 执行");
        System.out.println(System.currentTimeMillis());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        System.out.println("callDecode 执行");
        System.out.println(System.currentTimeMillis());
        super.callDecode(ctx, in, out);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() < 4){return ;}
//        list.add(byteBuf.readBytes(4));
        System.out.println("TimeDecoder 执行");
        list.add(new UnixTime(byteBuf.readUnsignedInt()));
    }

    @Override
    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decodeLast 执行");
        System.out.println(System.currentTimeMillis());
        super.decodeLast(ctx, in, out);
    }
}
