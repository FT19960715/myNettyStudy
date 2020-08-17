package com.service.HL7.myParse;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v24.segment.*;
import ca.uhn.hl7v2.parser.PipeParser;
import com.service.HL7.Helper.UserDefineMessage;
import com.service.HL7.XmlParse.HL7ToXmlConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MyParseHL7 {
    public static Message myParse(String msg) throws HL7Exception {
//        UserDefineMessage.ASType[] asTypes = new UserDefineMessage.ASType[7];
//        asTypes[0] = new UserDefineMessage.ASType(MSH.class,true,true);
//        asTypes[1] = new UserDefineMessage.ASType(MSA.class,true,true);
//        asTypes[2] = new UserDefineMessage.ASType(QAK.class,true,true);
//        asTypes[3] = new UserDefineMessage.ASType(QPD.class,true,true);
//        asTypes[4] = new UserDefineMessage.ASType(PID.class,true,true);
//        asTypes[5] = new UserDefineMessage.ASType(IN1.class,false,true);
//        asTypes[6] = new UserDefineMessage.ASType(QRI.class,false,true);
//        UserDefineMessage user = new UserDefineMessage(asTypes);
        PipeParser p = new PipeParser();
        Message message = p.parse(msg);
        return message;
    }
    public static void main(String[] args) {
//        String resStr = "MSH|^~\\&|EMR||Pivas||201605051542||OMP^O09^OMP_O09|5689a15d-57bb-4294-bd90-d02d246a2024|P|2.6|||NE|AL||utf-8  \r" +
//                "PID||12345619812|||小文^^^XIAO WEN||20151022|1  \r" +
//                "PV1||2|0207^0207H^43||||1385^小明||||||||||||1||||||||||||||||||||H0002|||||201605010850\r" +
//                "ORC|NW|248622||198148|||||20160505154156|||1366^ 小明||||||||| 儿 内 科 一 病 区^^0207||||||||A  \r" +
//                "TQ1|1||8&1/12 小时||||20160428095444|||1  \r" +
//                "RXO|001024^0.9%氯化钠注射液 100ml^^100ml@药业|100||12^ml||||||||||||||||0^否||||||||||||0506  \r" +
//                "RXR|B5^静滴  \r" +
//                "ORC|NW|248623||198148|||||20160505154156|||1366^ 小明 ||||||||| 儿 内 科 一 病 区^^0207||||||||A  \r" +
//                "TQ1|1||8&1/12 小时||||20160428095444|||1  \r" +
//                "RXO|000023^ 注射用头孢哌酮钠他唑巴坦钠 ( 新朗欧 )^^1g@三洋|0.4||9^g||||||||||||||||0^否||||||||||||0506  \r" +
//                "RXR|B5^静滴  ";
        String resStr = "MSH|^~\\&|SENDING_APPLICATION|SENDING_FACILITY|RECEIVING_APPLICATION|RECEIVING_FACILITY|20110614075841||ACK|1407511|P|2.4||||||\r\n" +
                "MSA|AA|1407511|Success||";
        String arr = "MSH|^~\\&|HOSP|HOSP|CLIN|CLIN|20071212113500-0600||RSP^K22^RSP_K21|1|D|2.5.1|\n" +
                "MSA|AA|8699|\n" +
                "QAK|111069|OK|Q22^Find Candidates|3|\n" +
                "QPD|Q22^Find Candidates|111069||80|MATCHWARE|1.2||^^^GOOD HEALTH HOSPITAL~^^^SOUTH\n" +
                "PID|||66785^^^GOOD HEALTH HOSPITAL~99999^^^SOUTH||JOHN\n" +
                "QRI|95||MATCHWARE_1.2|\n" +
                "PID|||87443^^^GOOD HEALTH HOSPITAL~651189^^^SOUTH||JOHN\n" +
                "QRI|90||MATCHWARE_1.2|\n" +
                "PID|||43266^^^GOOD HEALTH HOSPITAL~81209^^^SOUTH||JOHN\n" +
                "QRI|85||MATCHWARE_1.2|";
        try {
            Message message = myParse(arr);
//            System.out.println("message.getEncodingCharactersValue();:"+message.getEncodingCharactersValue());
//            System.out.println(message.getParser().encode(message));
            String names[] = message.getNames();
            System.out.println(HL7ToXmlConverter.ConvertToXml(arr));
            for(String name : names){
                System.out.println(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
