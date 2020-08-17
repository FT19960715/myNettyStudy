package com.service.HL7.simple;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTCPEchoServer {

    private static final int BUFFER_SIZE = 200;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1080);

        int receivedMessageSize;
        byte[] receivedByeBuffer = new byte[BUFFER_SIZE];

        while (true) {
            Socket clientSocket = serverSocket.accept();     // Get client connection

            System.out.println("Handling client at " +
                    clientSocket.getInetAddress().getHostAddress() + " through port " +
                    clientSocket.getPort());

            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            receivedMessageSize = in.read(receivedByeBuffer);
            out.write(receivedByeBuffer, 0, receivedMessageSize);


            clientSocket.close();  // Close the socket.  We are done serving this client
        }

    }
}
