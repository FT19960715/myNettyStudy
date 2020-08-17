//package com.service;
//
//import ch.ethz.ssh2.*;
//
//import java.io.*;
//
//public class FileSyncController {
//
//    String HOST_IP = "192.168.204.136";
//    String USER_NAME = "root";
//    String USER_PASSWORD = "root";
//
//    String SHELL_DIRECTIVE = "cd /home/test;"
//            +"ls -R";
//
//    public Connection getConnection() throws IOException {
//        Connection connection = new Connection(HOST_IP);
//        ConnectionInfo info = connection.connect();
//        boolean auth = connection.authenticateWithPassword(USER_NAME,USER_PASSWORD);
//
//        Session session = connection.openSession();
//        session.execCommand(SHELL_DIRECTIVE);
//
//
//        InputStream inputStream = new StreamGobbler(session.getStdout());
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        String resultLine;
//        while ((resultLine =reader.readLine())!=null){
//            System.out.println(resultLine);
//        }
//        System.out.println("ExitCode: "+session.getExitStatus());
//        session.close();
////        connection.close();
//        return connection;
//    }
//
//    public void mkdir(String origFile,String aimPath) throws IOException {
//
//    }
//    public void scp() throws IOException {
//        Connection connection = getConnection();
//        Session session = connection.openSession();
//        System.out.println("================= scp =================");
////        String SCP_CMD = String.format("scp %S root@10.1.24.72/home/test","/home/clinic/pic/img15/grp/person/1569296569270_286190.jpg");
//        session.execCommand("scp /home/clinic/pic/img15/grp/person/1569296569270_286190.jpg root@10.1.24.72:/home/test");
//        session.close();
//        connection.close();
//        System.out.println("=============== end =============");
//    }
//
//    public static void main(String[] args) throws IOException {
//
//        FileSyncController controller = new FileSyncController();
//        controller.scp();
////        controller.getConnection();
//    }
//}
