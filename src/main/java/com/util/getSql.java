package com.util;

import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// 获取数据库表的字段并拼接成sql的工具类
public class getSql {
    private static final String spacer = " as ";
    private static final String row = " \n ";
    private static final String comma = ",";

    private static String getUpdateSql(String tableName) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("update ").append(tableName).append(" set ").append(row);
        List<String> columnList = getClomunList(tableName);
        for(String a : columnList){
            buffer.append(" <if test="+user(a)+"!=null\"> ").append(a).append(" = #{").append(user(a)).append("},").append("</if>").append(row);
        }
        int lenOne = buffer.lastIndexOf(comma);
        String b = buffer.substring(0,lenOne);
        StringBuffer buffer1 = new StringBuffer(b);
        buffer1.append("</if>").append(row);
        return buffer1.toString();
    }

    private static String getAddSql(String tableName) throws SQLException {
        StringBuffer buffer= new StringBuffer();
        buffer.append("insert into ").append(tableName).append("(").append(row);
        List<String> columnList = getClomunList(tableName);
        for(String a : columnList){
            buffer.append(a).append(comma).append(row);
        }
        int lenOne = buffer.lastIndexOf(comma);
        String b = buffer.substring(0,lenOne); // 去掉最后一个逗号
        StringBuffer buffer1 = new StringBuffer(b);
        buffer1.append(row).append(")values(").append(row);
        for(String a :columnList){
            buffer1.append("#{").append(user(a)).append("}").append(comma).append(row);
        }
        int lenTwo = buffer1.lastIndexOf(comma);
        String c = buffer1.substring(0,lenTwo); // 去掉最后一个逗号
        StringBuffer buffer2 = new StringBuffer(c);
        buffer2.append(row).append(");");
        return buffer2.toString();
    }
    private static String getSql(String tableName) throws SQLException {
        List<String> columnList = getClomunList(tableName);
        StringBuffer buffer= new StringBuffer();
        buffer.append("select ").append("\n");
        for(String column:columnList){
            buffer.append(column).append(spacer).append(user(column)).append(comma).append(row);
        }
        int len = buffer.lastIndexOf(",");
        String c = buffer.substring(0,len);
        StringBuffer buffer1 = new StringBuffer(c);
        buffer1.append(row).append(" from ").append(tableName).append(row).append(" where logic_delete = 0");
        return buffer1.toString();
    }

    //获取驼峰命名
    public static String user(String str){
        if(!str.contains("_")){return str;}
        StringBuilder builder = new StringBuilder();
        //获取字符串中每个_修改为标准驼峰命名
        Arrays.asList(str.split("_")).forEach(temp -> builder.append(StringUtils.capitalize(temp)));
        return builder.toString().substring(0,1).toLowerCase()+builder.toString().substring(1,builder.toString().length());
    }
    private static List<String> getClomunList(String tableName) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet res = null;
        ResultSetMetaData reData = null;
        List<String> list = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.1.24.73:3306/cloud_clinic_dev_v1_7?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
                    "root","Ro998otPass&");
            String sql = "select COLUMN_NAME from information_schema.COLUMNS where table_name = "+"'"+tableName+"'"+"and table_schema = 'cloud_clinic_dev_v1_7'";
            pre = conn.prepareStatement(sql);
            res = pre.executeQuery();
            reData = res.getMetaData();
            while(res.next()){
                int i = 1;
                String value = res.getString(i).toLowerCase();
                list.add(value);
                i+=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.close();pre.close();res.close();
            conn = null; pre = null; res = null;  reData = null;
        }
        return list;
    }

    public static String getAdd( String tableName) throws SQLException {
        return getAddSql(tableName);
    }
    public static String getUpdate( String tableName) throws SQLException {
        return getUpdateSql(tableName);
    }
    public static String getSelect( String tableName) throws SQLException {
        return getSql(tableName);
    }

    public static void main(String[] args) {
        try {
            Object obj = getSql("info_clinic_drug");
            System.out.println(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


