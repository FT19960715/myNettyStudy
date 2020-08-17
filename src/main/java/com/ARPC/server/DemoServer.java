package com.ARPC.server;

import com.ARPC.Handler.ExpRequestHandler;
import com.ARPC.Handler.FibRequestHandler;
import com.ARPC.entity.ExpRequest;

public class DemoServer {
    public static void main(String[] args) {
        RPCServer server = new RPCServer("localhost", 8888, 2, 16);
        server.service("fib", Integer.class, new FibRequestHandler())
                .service("exp", ExpRequest.class, new ExpRequestHandler());
        server.start();
    }
}
