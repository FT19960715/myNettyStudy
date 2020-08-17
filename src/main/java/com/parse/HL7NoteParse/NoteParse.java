package com.parse.HL7NoteParse;

import ca.uhn.hl7v2.model.Message;

import java.io.*;

public class NoteParse {
    private final static String END = "</ul>";
    private final static String LI_START = "<li>";
    private final static String LI_END = "</li>";
    private final static String DELIMITER = ":";
    private final static String LINE = "\r";
    private final static String TABLE = " ";
    private final static String COLON = ":";
    // 获取文件列表
    public static File[] getFilesList(String path){
        File file = new File(path);
        return file.exists()?  file.listFiles():null;
    }
    public static File getFile(String path){
        File file = new File(path);
        return file.exists()?  file:null;
    }
    private static Object dealFile(File file,FileOutputStream outputStream ) throws IOException {
        String fileName = file.getName();
        System.out.println("## "+fileName);

        outputStream.write(("\n- "+fileName).getBytes());
        BufferedReader bu = new BufferedReader (new InputStreamReader(new FileInputStream(file)));
        while (bu.read() != -1){
            String line = bu.readLine();
            if(line.contains(LI_START)){
                line = line.replace("*","");
                line = line.replace(LI_START,"");
                line = line.replace(LI_END,"");
                if(isEmpty(line)){
                    // 没有内容什么都不做
                }else{
                    // 对字符春进行解析
                    if(line.contains(DELIMITER)){
                        StringBuffer buffer = new StringBuffer();
                        String arr[] = line.split(DELIMITER);
                        buffer.append(LINE).append("-").append(TABLE).append(arr[0]).append(COLON).append(arr[1]);
                        System.out.println(buffer.toString());
                        outputStream.write(buffer.toString().getBytes());
                    }
                }
            }else if(line.contains(END)){
                break;
            }else {
                continue;
            }
        }
        bu.close();
        return null;
    }

    private static  boolean isEmpty(String args){
        byte[] b = args.getBytes();
        for(byte b1 : b){
            if(!"".equals(b1)){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        File out = new File("F:\\项目\\中医诊所\\2.0版本\\周工作\\07\\20200727\\result.md");
        String path = "C:\\Users\\ft\\Desktop\\ca\\uhn\\hl7v2\\model\\v23\\group";
        FileOutputStream outputStream = new FileOutputStream(out);
        outputStream.write("## 文档".getBytes());
        File [] files = getFilesList(path);
        for(File file : files ){
            dealFile(file,outputStream);
        }
    }
}
