package com.parse.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class test {


    public static void listAll(List canditate, String prefix,List arr){
//        System.out.println(prefix) ;
        arr.add(prefix);
        for (int i=0;i<canditate.size();i++){
            List temp = new LinkedList(canditate);
            listAll(temp, prefix+ temp.remove(i),arr) ;
        }
    }

    public static void main(String [] args){
        int result = 0;
        String[]  array =new String[] {"1","2","1"};
        int length = array.length;
        List<String> arr = new ArrayList();
        listAll(Arrays.asList(array), "",arr) ;
        List<String> newList = new ArrayList();
        for(String obj :arr){if(obj.length() == length){newList.add(obj);}}
//        System.out.println(newList);
        for(String one : newList){
            if(Integer.parseInt(one)%7 == 0){
                result++;
            }
        }
        System.out.println(result);
    }

}
