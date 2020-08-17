package com.service.HL7.Helper;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DoNotCacheStructure;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;

@DoNotCacheStructure
public class UserDefineMessage  extends AbstractMessage {

    private static final long serialVersionUID = 1L;

    private static ASType[] types;

    private static ModelClassFactory fat = null;

    static {
        try {
            fat = new DefaultModelClassFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserDefineMessage(ModelClassFactory factory) throws HL7Exception {
        super(factory);
        init(types);
    }

    public UserDefineMessage(ASType... types) throws HL7Exception {
        super(fat == null ? new DefaultModelClassFactory() : fat);
        setASTypes(types);
        init(types);
    }

    private void init(ASType... types) throws HL7Exception {
        for (int i = 0; i < types.length; i++) {
            this.add(types[i].c, types[i].required, types[i].repeating);
        }
    }

    public String getVersion() {
        return "2.4";
    }

    public Structure getAS(String asName, Class<? extends Structure> asCls) {
        return getTyped(asName, asCls);
    }

    @SuppressWarnings("unchecked")
    public <T extends Structure> T getAS(Class<T> asCls) throws HL7Exception {
        return (T) get(asCls.getSimpleName());
    }

    public static class ASType {
        Class<? extends Structure> c;
        boolean required;
        boolean repeating;

        public ASType(Class<? extends Structure> c, boolean required, boolean repeating) {
            this.c = c;
            this.required = required;
            this.repeating = repeating;
        }
    }
    public String encode() throws HL7Exception {
        return super.encode();
    }

    private static void setASTypes(ASType... tys) {
        types = tys;
    }
}
