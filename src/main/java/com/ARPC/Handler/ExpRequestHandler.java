package com.ARPC.Handler;

import com.ARPC.entity.ExpRequest;
import com.ARPC.entity.ExpResponse;
import com.ARPC.entity.MessageOutput;
import io.netty.channel.ChannelHandlerContext;

public class ExpRequestHandler implements IMessageHandler<ExpRequest> {
    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, ExpRequest message) {
        int base = message.getBase();
        int exp = message.getExp();
        long start = System.nanoTime();
        long res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        long cost = System.nanoTime() - start;
        // 输出响应
        ctx.writeAndFlush(new MessageOutput(requestId, "exp_res", new ExpResponse(res, cost)));
    }
}
