package com.service.HL7.Helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ca.uhn.hl7v2.model.AbstractComposite;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v24.datatype.ST;
public class UserDefineComposite extends AbstractComposite {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static Class<? extends Type> defalutclass = ST.class;

    Type[] data = null;

    public UserDefineComposite(Message message) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        super(message);
        data = new Type[1];
        Constructor<? extends Type> cons = defalutclass.getConstructor(new Class[] { Message.class });
        data[0] = (Type) cons.newInstance(new Object[] { getMessage() });
    }

    public UserDefineComposite(Message message, Type... types) {
        super(message);
        init(types);
    }

    @SuppressWarnings("rawtypes")
    public UserDefineComposite(Message message, Class... clss) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        super(message);
        init(clss);
    }

    @SuppressWarnings("rawtypes")
    public UserDefineComposite(Message message, int typeCount) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        super(message);
        Class[] clss = new Class[typeCount];
        data = new Type[typeCount];
        Constructor<? extends Type> cons = defalutclass.getConstructor(new Class[] { Message.class });
        for (int i = 0; i < clss.length; i++) {
            data[i] = (Type) cons.newInstance(new Object[] { getMessage() });
        }
    }

    private void init(Type... types) {
        data = new Type[types.length];
        for (int i = 0; i < types.length; i++) {
            data[i] = types[i];
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void init(Class... clss) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        data = new Type[clss.length];
        for (int i = 0; i < clss.length; i++) {
            Constructor<? extends Type> cons = clss[i].getConstructor(new Class[] { Message.class });
            data[i] = (Type) cons.newInstance(new Object[] { getMessage() });
        }
    }

    public Type[] getComponents() {
        return data;
    }

    public Type getComponent(int number) throws DataTypeException {
        try {
            return this.data[number];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataTypeException("Element " + number + " doesn't exist (Type " + getClass().getName()
                    + " has only " + this.data.length + " components)");
        }
    }
}
