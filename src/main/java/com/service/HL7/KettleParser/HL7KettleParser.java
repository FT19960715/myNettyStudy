package com.service.HL7.KettleParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.SegmentFinder;
import ca.uhn.hl7v2.util.Terser;

public class HL7KettleParser {

    public static List<HL7Value> extractValues( Message message ) throws Exception {
        Terser terser = new Terser( message );
        SegmentFinder finder = terser.getFinder();

        List<HL7Value> values = new ArrayList<HL7Value>();

        int childNr = 1;

        while ( finder.hasNextChild() ) {

            // next group in the message (MSH, PID, EVN and so on)
            //
            finder.nextChild();
            Structure[] structures = finder.getCurrentChildReps();
            for ( int i = 0; i < structures.length; i++ ) {
                Structure structure = structures[i];
                parseStructure( values, message, terser, structure, Integer.toString( childNr ) );
            }

            childNr++;
        }

        return values;
    }

    private static void parseStructure( List<HL7Value> values, Message message, Terser terser, Structure structure,
                                        String structureNumber ) throws Exception {

        Map<String, List<String>> nameMap = NamesUtil.getInstance().getMap();

        if ( structure instanceof Segment ) {

            Segment segment = (Segment) structure;
            String[] names = segment.getNames();

            for ( int n = 1; n <= segment.numFields(); n++ ) {
                Type[] types = segment.getField( n );
                for ( int t = 0; t < types.length; t++ ) {
                    int nrComponents = Terser.numComponents( types[t] );
                    for ( int c = 1; c <= nrComponents; c++ ) {
                        int nrSub = Terser.numSubComponents( types[t], c );
                        for ( int sc = 1; sc <= nrSub; sc++ ) {
                            String string = Terser.get( segment, n, t, c, sc );
                            // Primitive primitive = Terser.getPrimitive(types[t], c, sc);

                            String description = "?";
                            List<String> list = nameMap.get( types[t].getName() );
                            if ( list != null && c - 1 < list.size() ) {
                                description = list.get( c - 1 );
                            }

                            Group group = structure.getParent();
                            Group rootGroup = structure.getMessage();

                            String coordinates = n + "." + ( t + 1 ) + "." + c + "." + sc;

                            HL7Value value =
                                    new HL7Value( message.getVersion(), rootGroup.getName(), group.getName(), structure.getName(),
                                            structureNumber, names[n - 1], coordinates, types[t].getName(), description, string );
                            values.add( value );
                        }
                    }
                }
            }

        } else if ( structure instanceof Group ) {
            Group group = (Group) structure;

            String[] names = group.getNames();

            for ( int n = 1; n <= names.length; n++ ) {
                String name = names[n - 1];
                Structure subStructure = group.get( name );
                parseStructure( values, message, terser, subStructure, structureNumber + "." + n );
            }
        } else {
            throw new Exception( "oops, not handled yet!" );
        }

    }

    public static void main(String[] args) throws Exception {
        String resStr = "MSH|^~\\&|EMR||Pivas||201605051542||OMP^O09^OMP_O09|5689a15d-57bb-4294-bd90-d02d246a2024|P|2.6|||NE|AL||utf-8  \r" +
                "PID||12345619812|||小文^^^XIAO WEN||20151022|1  \r" +
                "PV1||2|0207^0207H^43||||1385^小明||||||||||||1||||||||||||||||||||H0002|||||201605010850\r" +
                "ORC|NW|248622||198148|||||20160505154156|||1366^ 小明||||||||| 儿 内 科 一 病 区^^0207||||||||A  \r" +
                "TQ1|1||8&1/12 小时||||20160428095444|||1  \r" +
                "RXO|001024^0.9%氯化钠注射液 100ml^^100ml@药业|100||12^ml||||||||||||||||0^否||||||||||||0506  \r" +
                "RXR|B5^静滴  \r" +
                "ORC|NW|248623||198148|||||20160505154156|||1366^ 小明 ||||||||| 儿 内 科 一 病 区^^0207||||||||A  \r" +
                "TQ1|1||8&1/12 小时||||20160428095444|||1  \r" +
                "RXO|000023^ 注射用头孢哌酮钠他唑巴坦钠 ( 新朗欧 )^^1g@三洋|0.4||9^g||||||||||||||||0^否||||||||||||0506  \r" +
                "RXR|B5^静滴  ";
        PipeParser p = new PipeParser();
        Message message = p.parse(resStr);
        List list = extractValues(message);
        for(Object object : list){
            System.out.println(object);
        }
    }

}