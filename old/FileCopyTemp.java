package com.service;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class FileCopy {
    // 本机linux
    static String HOST_IP = "192.168.204.136";
    static String USER_NAME = "root";
    static String USER_PASSWORD = "root";


    private static String main = " root@10.1.24.72:";
    private static String test = " root@10.1.24.73:";


    private static String line = "\n";
    private static String scp = "scp  ";

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
    public static String getSession(String commond,Connection connection ) throws IOException {
//        Connection connection = null;
        Session session = null;
        InputStream inputStream  = null;
        BufferedReader reader  = null;
        StringBuffer buffer = new StringBuffer();
        try {
//            connection = getConnection(HOST_IP,USER_NAME,USER_PASSWORD);
            session = connection.openSession();
            session.execCommand(commond);
            inputStream = new StreamGobbler(session.getStdout());
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String resultLine ;
            while ((resultLine =reader.readLine())!=null){
                buffer.append(resultLine).append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(connection != null){
                connection.close();
            }  if(session != null){
                session.close();
            }  if(inputStream != null){
                inputStream.close();
            } if(reader != null){
                reader.close();
            }
        }finally {
            if(connection != null){
                connection.close();
            }  if(session != null){
                session.close();
            }  if(inputStream != null){
                inputStream.close();
            } if(reader != null){
                reader.close();
            }
        }
        return buffer.toString();
    }

    /***
     * 处理从服务器读取道德两个文件
     * @param file1 生产服务器
     * @param file2 测试服务器
     */
    protected static void dealTowFIle(String file1,String file2){
        List<String> list1 = Arrays.asList(file1.split("\n"));  // 生产服务器文件集合
        List<String> list2 = Arrays.asList(file2.split("\n"));  // 测试服务器文件集合
        Map<String,String> oneMap= new HashMap(); // 生产map集合
        Map <String,String> twoMap= new HashMap();    // 测试map集合
        for(String item : list1){
            String a = item.substring(item.lastIndexOf("/")+1);
            oneMap.put(a,item);
        } for(String item : list2){
            String a = item.substring(item.lastIndexOf("/")+1);
            twoMap.put(a,item);
        }
//        list1.forEach(item->{
//            String a = item.substring(item.lastIndexOf("/")+1);
//            oneMap.put(a,item);
//        });
//        list2.forEach(item->{
//            String a = item.substring(item.lastIndexOf("/")+1);
//            twoMap.put(a,item);
//        });
        dealTwoMap(oneMap,twoMap);
    }

    protected static void dealTwoMap(Map<String,String> bakmap , Map<String,String> mainmap){
        List<String> baklist1 = dealMap2List(bakmap);
        List<String> baklist1copy = new ArrayList(baklist1);
        List<String> baklist2 = dealMap2List(mainmap);
        List<String> baklist2copy = new ArrayList(baklist2);
        // 互相排重
        baklist1.removeAll(baklist2copy);
        baklist2.removeAll(baklist1copy);
        // TODO: 2020-05-19 这个baklist1中的是73服务器有但是72服务器没有文件，相反，baklist2是72服务器有但是73服务器没有的代码
        doScript(bakmap,baklist1);
    }

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


    public static void doScript(Map<String,String> map ,List<String> list){
//        buffer.append(line);
        for(String a :list){
            StringBuffer buffer = new StringBuffer();
            String b = map.get(a);
            String c = b.substring(0,b.lastIndexOf("/"));

            buffer.append(scp).append(main).append(b).append("    ").append(c).append("  ").append(line);
//            System.out.println("buffer data");
            System.out.println(buffer.toString());
            File f1 = new File(c);
            if(!f1.exists()){f1.mkdir();}
            Connection conn = null;
            try {
                conn = getConnectionTest();
                Object obj = getSession(buffer.toString(),conn);
                System.out.println(obj);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static boolean isIpAdd(String str){
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }
    private static boolean isDictionary(String str){
        return str.startsWith("/");
    }

    public static void main(String[] args) throws IOException {


        String help ="/*** \n"
         + "* 本程序用于将指定两个服务器文件路径下的文件，双向同步"
         + " * 入参说明  : \n"
         + " * args[0] sync  / help \n"
         + " * args[1] server1 host 服务器1 IP地址\n"
         + " * args[2] server1 dictionary  服务器1 文件路径\n"
         + " * args[3] server2 host 服务器2 IP地址\n"
         + " * args[4] server2 dictionary  服务器2 文件路径\n"
        + " * args[3] duration \n"
         + " * */\n";
        if(args.length  == 4 || args.length == 1){
            if(args.length == 1){
                if("help".equals(args[0]) || "h".equals(args[0])) System.out.println(help);
                else System.out.println("指令 help 查看帮助");
            }else{
                if("sync".equals(args[0])){
                    String hostA = args[1];
                    String hostB = args[3];
                    String duration = args[3];
                    // TODO: 2020-05-26 使用正则验证 host 是 ip地址
                    if(!isIpAdd(hostA)){
                        // 不是IP地址
                    }
                    if(!isIpAdd(hostB)){
                        // 不是IP地址
                    }
                    if(!isDictionary(duration)){
                        // 不是文件目录
                    }
                }
            }
        }else{

        }

        Connection conn = getConnectionMain();// 获取72 链接
        String obj = getSession(commond,conn);
        // 这里的obj是所有本地下的文件
        Connection conn1 = getConnectionTest();
        String obj1 = getSession(commond,conn1);
        // 获取到72服务器和73服务器下的所有文件名称并转换成字符串

//        System.out.println("obj");
//        System.out.println(obj);
//        System.out.println(obj1);
        dealTowFIle(obj,obj1);
    }

}
