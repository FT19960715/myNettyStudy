package com.service.HL7.Thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SimpleThreadedEchoServer {

    private int listenPort;

    public SimpleThreadedEchoServer(int aListenPort) {
        listenPort = aListenPort;
    }

    public static void main(String[] args) {
        SimpleThreadedEchoServer server = new SimpleThreadedEchoServer(1080);
        server.acceptIncomingConnections();
    }

    private void acceptIncomingConnections() {
        try {
            ServerSocket server = new ServerSocket(listenPort, 5); //Accept up to 5 clients in the queue
            System.out.println("Server has been started");
            Socket clientSocket = null;
            while (true) {
                clientSocket = server.accept();
                handleIncomingConnection(clientSocket);
            }
        } catch (BindException e) {
            System.out.println("Unable to bind to port " + listenPort);
        } catch (IOException e) {
            System.out.println("Unable to instantiate a ServerSocket on port: " + listenPort);
        }
    }

    protected void handleIncomingConnection(Socket aConnectionToHandle) {
        new Thread(new ConnectionHandler(aConnectionToHandle)).start();
    }

    private static class ConnectionHandler implements Runnable {
        private Socket connection;
        private int receivedMessageSize;
        private byte[] receivedByeBuffer = new byte[BUFFER_SIZE];
        private static final int BUFFER_SIZE = 32;

        public ConnectionHandler(Socket aClientSocket) {
            connection = aClientSocket;
        }

        public void run() {
            try {
                System.out.println("Handling client at " + connection.getInetAddress().getHostAddress()
                        + " on port " + connection.getPort());

                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream();

                receivedMessageSize = in.read(receivedByeBuffer);
                out.write(receivedByeBuffer, 0, receivedMessageSize);

                connection.close();  // Close the socket.  We are done serving this client

            } catch (IOException e) {
                System.out.println("Error handling a client: " + e);
            }
        }
    }
    /***
     * 处理接收到的消息
     * @param anInputStream
     * @return
     * @throws IOException
     */
//    public String getMessage(InputStream anInputStream) throws IOException  {
//
//        boolean end_of_message = false;
//        StringBuffer parsedMessage = new StringBuffer();
//
//        int characterReceived = 0;
//
//        try {
//            characterReceived = anInputStream.read();
//        } catch (SocketException e) {
//            System.out
//                    .println("Unable to read from socket stream. "
//                            + "Connection may have been closed: " + e.getMessage());
//            return null;
//        }
//
//        if (characterReceived == END_OF_TRANSMISSION) {
//            return null;
//        }
//
//        if (characterReceived != START_OF_BLOCK) {
//            throw new RuntimeException(
//                    "Start of block character has not been received");
//        }
//
//        while (!end_of_message) {
//            characterReceived = anInputStream.read();
//
//            if (characterReceived == END_OF_TRANSMISSION) {
//                throw new RuntimeException(
//                        "Message terminated without end of message character");
//            }
//
//            if (characterReceived == END_OF_BLOCK) {
//                characterReceived = anInputStream.read();
//
//                if (characterReceived != CARRIAGE_RETURN) {
//                    throw new RuntimeException(
//                            "End of message character must be followed by a carriage return character");
//                }
//                end_of_message = true;
//            } else {
//                parsedMessage.append((char) characterReceived);
//            }
//        }
//
//        return parsedMessage.toString();
//    }

//    private String getSimpleAcknowledgementMessage(String aParsedHL7Message) {
//        if (aParsedHL7Message == null)
//            throw new RuntimeException("Invalid HL7 message for parsing operation. Please check your inputs");
//
//        String messageControlID = getMessageControlID(aParsedHL7Message);
//
//        StringBuffer ackMessage = new StringBuffer();
//        ackMessage = ackMessage.append(START_OF_BLOCK)
//                .append("MSH|^~\\&|||||||ACK||P|2.2")
//                .append(CARRIAGE_RETURN)
//                .append("MSA|AA|")
//                .append(messageControlID)
//                .append(CARRIAGE_RETURN)
//                .append(END_OF_BLOCK)
//                .append(CARRIAGE_RETURN);
//
//        return ackMessage.toString();
//    }
//
//    private String getMessageControlID(String aParsedHL7Message) {
//        int fieldCount = 0;
//        StringTokenizer tokenizer = new StringTokenizer(aParsedHL7Message, FIELD_DELIMITER);
//
//        while (tokenizer.hasMoreElements())
//        {
//            String token = tokenizer.nextToken();
//            fieldCount++;
//            if (fieldCount == MESSAGE_CONTROL_ID_LOCATION){
//                return token;
//            }
//        }
//
//        return "";
//    }
}
