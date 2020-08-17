package com.service;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class FileCopy {
    // TODO: 2020-05-27 这里我想偷懒，不想引入日志系统，将日志输出到控制台，然后使用shell脚本将控制台输出日志输出到固定日志文件中去
    private static String main = " root@10.1.24.72:";
    private static String test = " root@10.1.24.73:";

    public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static final String debug = "DEBUG";
    private static final String info = "INFO";
    private static final String warning = "WARNING";
    private static final String error = "ERROR";

    private static String getNowDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }



    public static String getHash(String fileName, String hashType)
            throws Exception {
        InputStream fis;
        fis = new FileInputStream(fileName);
        byte[] buffer = new byte[1024];
        MessageDigest md5 = MessageDigest.getInstance(hashType);
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            md5.update(buffer, 0, numRead);
        }
        fis.close();
        return toHexString(md5.digest());
    }

    /**
     * md5转成字符串
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static String line = "\n";
//    private static String scp = "scp  ";

    static String commond = "find /home/fengtao/temp -type f";
    // find /home/clinic/pic -type f -mtime +20 查询最近二十天
    // find /home/clinic/pic -type f -mmin +20 查询最近二十分钟
//    static String commond1 = "find /home/script -type f";


    public static Connection getConnection(String host,String name,String password) throws IOException {
        Connection connection = new Connection(host);
        ConnectionInfo info = connection.connect();
        boolean auth = connection.authenticateWithPassword(name,password);
        return connection;
    }

    /**
     * 72
     * @return
     * @throws IOException
     */
    public static Connection getConnectionMain() throws IOException {
        Connection connection = new Connection("10.1.24.72");
        ConnectionInfo info = connection.connect();
        boolean auth = connection.authenticateWithPassword("root","bicon@123");
        return connection;
    }

    /**
     * 73
     * @return
     * @throws IOException
     */
    public static Connection getConnectionTest() throws IOException {
        Connection connection = new Connection("10.1.24.73");
        ConnectionInfo info = connection.connect();
        boolean auth = connection.authenticateWithPassword("root","bicon@123");
        return connection;
    }

    public static String getExecResult(String command, Connection connection ) throws IOException {
//        Connection connection = null;
        Session session = null;
        InputStream inputStream  = null;
        BufferedReader reader  = null;
        StringBuffer buffer = new StringBuffer();
        try {
//            connection = getConnection(HOST_IP,USER_NAME,USER_PASSWORD);
            session = connection.openSession();
            session.execCommand(command);
            inputStream = new StreamGobbler(session.getStdout());
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String resultLine ;
            while ((resultLine =reader.readLine())!=null){
                buffer.append(resultLine).append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
//            if(connection != null){
//                connection.close();
//            }
            if(session != null){
                session.close();
            }  if(inputStream != null){
                inputStream.close();
            } if(reader != null){
                reader.close();
            }
        }finally {
//            if(connection != null){
//                connection.close();
//            }
            if(session != null){
                session.close();
            }  if(inputStream != null){
                inputStream.close();
            } if(reader != null){
                reader.close();
            }
        }
         return buffer.toString();
    }

//    protected static void dealTowFIle(String file1,String file2){
//        List<String> list1 = Arrays.asList(file1.split("\n"));  // 生产服务器文件集合
//        List<String> list2 = Arrays.asList(file2.split("\n"));  // 测试服务器文件集合
//        Map<String,String> oneMap= new HashMap(); // 生产map集合
//        Map <String,String> twoMap= new HashMap();    // 测试map集合
//        for(String item : list1){
//            String a = item.substring(item.lastIndexOf("/")+1);
//            oneMap.put(a,item);
//        } for(String item : list2){
//            String a = item.substring(item.lastIndexOf("/")+1);
//            twoMap.put(a,item);
//        }
////        list1.forEach(item->{
////            String a = item.substring(item.lastIndexOf("/")+1);
////            oneMap.put(a,item);
////        });
////        list2.forEach(item->{
////            String a = item.substring(item.lastIndexOf("/")+1);
////            twoMap.put(a,item);
////        });
//        dealTwoMap(oneMap,twoMap);
//    }

//    protected static void dealTwoMap(Map<String,String> bakmap , Map<String,String> mainmap){
//        List<String> baklist1 = dealMap2List(bakmap);
//        List<String> baklist1copy = new ArrayList(baklist1);
//        List<String> baklist2 = dealMap2List(mainmap);
//        List<String> baklist2copy = new ArrayList(baklist2);
//        // 互相排重
//        baklist1.removeAll(baklist2copy);
//        baklist2.removeAll(baklist1copy);
//        // TODO: 2020-05-19 这个baklist1中的是73服务器有但是72服务器没有文件，相反，baklist2是72服务器有但是73服务器没有的代码
//        doScript(bakmap,baklist1);
//    }

    private static List<String> dealMap2List(Map<String,String> map){
        List<String> list = new ArrayList();
        Map.Entry entry = null;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()){
            entry = (Map.Entry) it.next();
            list.add(entry.getKey().toString());
        }
        return list;
    }


//    public static void doScript(Map<String,String> map ,List<String> list){
////        buffer.append(line);
//        for(String a :list){
//            StringBuffer buffer = new StringBuffer();
//            String b = map.get(a);
//            String c = b.substring(0,b.lastIndexOf("/"));
//
//            buffer.append(scp).append(main).append(b).append("    ").append(c).append("  ").append(line);
//            System.out.println(buffer.toString());
//            File f1 = new File(c);
//            if(!f1.exists()){f1.mkdir();}
//            Connection conn = null;
//            try {
//                conn = getConnectionTest();
//                Object obj = getExecResult(buffer.toString(),conn);
//                System.out.println(obj);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    private static boolean isIpAdd(String str){
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }
    private static boolean isDictionary(String str){
        return str.startsWith("/");
    }
    private static boolean check(Connection connectionA,String dirA,Connection connectionB ,String dirB){
        // TODO: 2020-05-26 确认两个dir存在 ，且 copy.lock 存在，密钥相等
        File fileA = new File(dirA);
        File fileB = new File(dirB);
        if(!fileA.exists()){ // 目录不存在
            System.out.println(String.format("%s  %s  目录 %s 不存在",getNowDate(),error,dirA));
            return false;
        }
        if(!fileB.exists()){
            System.out.println(String.format("%s  %s  目录 %s 不存在",getNowDate(),error,dirB));
            return false;
        }
        String pathA = dirA+"/copy.lock";
        String pathB = dirB+"/copy.lock";
        File keyA = new File(pathA);
        File keyB = new File(pathB);
//        if(!keyA.exists()){ // 没有密钥文件
//            System.out.println(String.format("%s %s  密钥文件 %s 不存在",getNowDate(),error,pathA));
//            return false;
//        }
//        if(!keyB.exists()){
//            System.out.println(String.format("%s %s  密钥文件 %s 不存在",getNowDate(),error,pathB));
//            return false;
//        }
        String hashPathA = null;
        String hashPathB = null;
        try {
            hashPathA = getHash(pathA,"MD5");
            hashPathB = getHash(pathB,"MD5");
        }catch (Exception e){

        }
//        if (!hashPathA.equals(hashPathB)){
//            System.out.println(String.format("%s %s  密钥文件不相同",getNowDate(),error));
//        }
        System.out.println(String.format("%s %s  文件目录校验通过",getNowDate(),info));
        return true;
    }
    private static void doCopy(Connection connectionA,String dirA,String userA,String hostA,Connection connectionB,String dirB,String duration,String userB,String hostB) throws IOException {
        List<String> listA = getUpdatedSubFileList(connectionA,dirA,duration); // A服务器所有的文件及目录
        List<String> listB = getUpdatedSubFileList(connectionB,dirB,duration);  // B服务器所有的文件及目录
        System.out.println(String.format("%s  %s  A服务器所有的文件及目录 %s",getNowDate(),info,listA));
        System.out.println(String.format("%s  %s  B服务器所有的文件及目录 %s",getNowDate(),info,listB));
        List<String> aMoveToB = new ArrayList<String>();
        List<String> bMoveToA = new ArrayList<String>();
        for (String bfile :
                listB) {
            // TODO: 2020-05-26  这里
            if(!listA.contains(bfile)){
                bMoveToA.add(bfile);
                System.out.println(String.format("%s  %s  B服务器有A服务器没有的文件 %s",getNowDate(),info,bfile));
            }
        }
        for (String afile :
                listA) {
            if(!listB.contains(afile)) {
                aMoveToB.add(afile);
                System.out.println(String.format("%s  %s  A服务器有B服务器没有的文件 %s",getNowDate(),info,afile));
            }

        }
        if(aMoveToB != null || !aMoveToB.isEmpty()){
            scpFiles(connectionA,dirA,aMoveToB,dirB,userB,hostB);
        }else{
            System.out.println(String.format("%s  %s  aMoveToB的文件未null",getNowDate(),info));
        }
        if(bMoveToA != null || !bMoveToA.isEmpty()){
            scpFiles(connectionB,dirB,bMoveToA,dirA,userA,hostA);
        }else{
            System.out.println(String.format("%s  %s  bMoveToA的文件未null",getNowDate(),info));
        }
    }
    private static void scpFiles(Connection source,String sourceDir,List<String> sourceFileList,String aimDir,String aimUser,String aimHost) throws IOException {
        for (String sourceSubFilePath  :
                sourceFileList ) {
            String scpCommand = getScpCommand(sourceDir,sourceSubFilePath,aimUser,aimHost,aimDir);
            String result = getExecResult(scpCommand,source);
            // TODO: 2020-05-26 此处或许可以验证 scp 是否成功
            System.out.println(String.format("%s  %s  copy 命令:%s",getNowDate(),info,scpCommand));
        }
    }
    private static String getFindUpdatedCommand(String rootDir,String duration){
        // TODO: 2020-05-27 我在这个地方做了一些小的判断，当传入的时间未空的时候表示不关心时间，直接复制所有文件
        if(duration == null || "null".equals(duration)){
            System.out.println(String.format("%s  %s  复制文件 find %s -type f",getNowDate(),info,rootDir));
            return String.format("find %s -type f",rootDir);
        }else{
            System.out.println(String.format("%s  %s  复制文件 find %s -type f -mmin %s ",getNowDate(),info,rootDir,duration));
            return String.format("find %s -type f -mmin +%s",rootDir,duration);
        }
    }
    private static String getScpCommand(String sourceRoot,String sourceFileSubPath, String aimUser,String aimHost,String aimRoot){
        String sourcePath = sourceRoot+sourceFileSubPath;
        String aimPath = aimRoot + sourceFileSubPath;
        return String.format("scp %s %s@%s:%s",sourcePath,aimUser,aimHost,aimPath);
    }
    private static List<String> getUpdatedSubFileList(Connection connection, String dir, String duration) throws IOException {
        String command = getFindUpdatedCommand(dir,duration);
        String listStirng = getExecResult(command,connection);
        System.out.println(String.format("%s  %s  服务器下的文件：%s",getNowDate(),info,listStirng));
        List<String> files = new ArrayList<String>();
        if(!"".equals(listStirng)){
//            非空字符串
            files = Arrays.asList(listStirng.split("\n"));
        }

        List<String> subFiles = new ArrayList<String>();
        int index = dir.length();
        for (String file   :
                files ) {
            subFiles.add(file.substring(index)); // 这个地方的subString可能有问题，需要确认
        }
        return subFiles;
    }
    public static class HostInfo{
        public String user;
        public String password;
        public String host;
        public String baseRoot;
    }
    public static void main(String[] args) {
        Connection connectionA = null;
        Connection connectionB = null;
        try {
            String help = getNowDate() + "   /*** \n"
                    + "* 本程序用于将指定两个服务器文件路径下的文件，双向同步"
                    + "请保证两个服务器下的指定目录中，存在 copy.lock 文件，并且其中 密钥文本相等"
                    + "注意：需自行保证两台服务器开启SSH服务,且互信"
                    + " * 入参说明  : \n"
                    + " * args[0] sync  / help \n"
                    + " * args[1] server1 host 服务器1 IP地址\n"
                    + " * args[2] server1 user 服务器1 用户名\n"
                    + " * args[3] server1 password 服务器1 密码\n"
                    + " * args[4] server1 dictionary  服务器1 文件路径\n"
                    + " * args[5] server2 host 服务器2 IP地址\n"
                    + " * args[6] server2 user 服务器1 用户名\n"
                    + " * args[7] server2 password 服务器1 密码\n"
                    + " * args[8] server2 dictionary  服务器2 文件路径\n"
                    + " * args[9] duration number字符串，单位分钟。最近时间段被修改过的文件\n"
                    + " * */\n";
            if (args.length == 10 || args.length == 1 || args.length == 9) { // 参数正确 或 help，我想吧时间做成可传可不传的那种，这样没传入时间的时候，查询的指令就应该是没有时间的那种
                if (args.length == 1) {
                    System.out.println(args[0]);
                    if ("help".equals(args[0]) || "h".equals(args[0])) {
                        System.out.println(help);
                    } else System.out.println(getNowDate() + "  指令 help 查看帮助");
                } else {
                    String B = "";
                    for (String a : args) {
                        B += a + "  ";
                    }
                    System.out.println(String.format("%s  %s  请求参数：%s", getNowDate(), warning, B));
                    if ("sync".equals(args[0])) {
                        String hostA = args[1];
                        String userA = args[2];
                        String passA = args[3];
                        String dirA = args[4];

                        String hostB = args[5];
                        String userB = args[6];
                        String passB = args[7];
                        String dirB = args[8];
                        String duration = null;
//                    String hostA = "10.1.24.72";
//                    String userA = "root";
//                    String passA = "bicon@123";
//                    String dirA = "/home/fengtao/temp";
//
//                    String hostB = "10.1.24.73";
//                    String userB = "root";
//                    String passB = "bicon@123";
//                    String dirB = "/home/fengtao/temp";
//                    String duration = null;
                        if (args.length == 10 && !"null".equals(args[9])) {
                            duration = args[9]; // 时间，分钟，我想做成可传可不传的那种
                        }
                        // TODO: 2020-05-26 使用正则验证 host 是 ip地址
                        if (!isIpAdd(hostA)) {
                            // 不是IP地址
                            System.out.println(String.format("%s  %s  %s不是可用ip地址", getNowDate(), warning, hostA));
                        }
                        if (!isIpAdd(hostB)) {
                            // 不是IP地址
                            System.out.println(String.format("%s  %s  %s不是可用ip地址", getNowDate(), warning, hostB));
                        }
                        if (!isDictionary(dirA)) {
                            // 不是文件目录
                            System.out.println(String.format("%s  %s  %s不是文件目录", getNowDate(), warning, dirA));
                        }
                        // 初始化两个服务连接
                        // 日志输出传入参数
                        System.out.println(String.format("%s  %s  hostA:%s userA:%s dirA:%s", getNowDate(), info, hostA, userA, dirA));
                        System.out.println(String.format("%s  %s  hostB:%s userB:%s dirB:%s", getNowDate(), info, hostB, userB, dirB));
                        connectionA= getConnection(hostA, userA, passA);
                        connectionB = getConnection(hostB, userB, passB);
                        // 验证两个目录存在，存在并验证 copy.lock
                        if (check(connectionA, dirA, connectionB, dirB)) {
                            // 进行文件互拷贝
                            doCopy(connectionA, dirA, userA, hostA, connectionB, dirB, duration, userB, hostB);
                            // 完成
                        } else {

                        }
                        // 关闭 connection
                    }
                }
            } else { // 参数错误
                System.out.println(String.format("%s  %s  参数长度错误  %s", getNowDate(), error, args.length));
            }

//        Connection conn = getConnectionMain();// 获取72 链接
//        String obj = getExecResult(commond,conn);
//        // 这里的obj是所有本地下的文件
//        Connection conn1 = getConnectionTest();
//        String obj1 = getExecResult(commond,conn1);
//        // 获取到72服务器和73服务器下的所有文件名称并转换成字符串
//
////        System.out.println("obj");
////        System.out.println(obj);
////        System.out.println(obj1);
//        dealTowFIle(obj,obj1);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(connectionA!=null){
                connectionA.close();
                connectionA=null;
            }
            if(connectionB!=null){
                connectionB.close();
                connectionB=null;
            }
        }
    }
}
