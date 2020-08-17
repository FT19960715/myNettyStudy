//package com.service;
//
//import java.io.*;
//import java.util.*;
//
//public class service {
//
//    private final static String ONE = "E://a.text";
////    private final static String ONE = "/home/fengtao/file/a.text";
//    private final static String TWO = "E:/b.text";
////    private final static String TWO = "/home/fengtao/file/b.text";
//
//    private final static String SCRIPT_POSTION = "/home/fengtao/script/copyfile.sh";
//    private final static String line = "\n";
//    private final static String scp = "scp  ";
//
//    private final static String main = " root@10.1.24.72:";
//
//
//    protected void dealFile() throws Exception{
//        Map <String,String> oneMap= new HashMap<>();
//        Map <String,String> twoMap= new HashMap<>();
//        BufferedReader reader1 = new BufferedReader(new FileReader(ONE));
//        BufferedReader reader2 = new BufferedReader(new FileReader(TWO));
//        String line;
//        String line1;
//        while ((line = reader1.readLine() )!= null){
//            String a = line.substring(line.lastIndexOf("/")+1,line.lastIndexOf("."));
//            oneMap.put(a,line);
////            oneList.add(a);
//        }
//        while ((line1 = reader2.readLine() )!= null){
//            String b = line1.substring(line1.lastIndexOf("/")+1,line1.lastIndexOf("."));
//            twoMap.put(b,line1);
////            twoList.add(b);
//        }
//        System.out.println("oneMap:"+oneMap);
//        System.out.println("twoMap:"+twoMap);
//        dealTwoMap(oneMap,twoMap);
//    }
//
//    /**
//     *
//     * @param bakmap 备份map 这里指73
//     * @param mainmap 主力服务器 这里指 72
//     */
//    protected void dealTwoMap(Map<String,String> bakmap , Map<String,String> mainmap){
//        List<String> baklist1 = dealMap2List(bakmap);
//        List<String> baklist1copy = new ArrayList<>(baklist1);
//        List<String> baklist2 = dealMap2List(mainmap);
//        List<String> baklist2copy = new ArrayList<>(baklist2);
//        // 互相排重
//        baklist1.removeAll(baklist2copy);
//        baklist2.removeAll(baklist1copy);
//        // TODO: 2020-05-19 这个baklist1中的是73服务器有但是72服务器没有文件，相反，baklist2是72服务器有但是73服务器没有的代码
//        printScript(bakmap,baklist1);
////        printScript(mainmap,baklist2);
//    }
//
//
//    public static List<String> dealMap2List(Map<String,String> map){
//        List<String> list = new ArrayList();
//        Map.Entry entry = null;
//        Iterator it = map.entrySet().iterator();
//        while (it.hasNext()){
//            entry = (Map.Entry) it.next();
//            list.add(entry.getKey().toString());
//        }
//        return list;
//    }
//
//    /**
//     * 生成脚本文件，生成的脚本文件需要手动去执行
//     * @param map
//     * @param list
//     */
//    public static void printScript(Map<String,String> map ,List<String> list){
//        StringBuffer buffer = new StringBuffer("echo \"script is ready to use000\"");
//        buffer.append(line);
//        for(String a :list){
//            String b = map.get(a);
//            buffer.append(scp).append(b).append(main).append(b).append("  ").append(line);
//        }
//        System.out.println("buffer data");
//        System.out.println(buffer.toString());
//    }
//
//    public static void main(String[] args) throws Exception {
//        service s = new service();
//        s.dealFile();
//    }
//}
