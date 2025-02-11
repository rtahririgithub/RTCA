package com.telus.credit.crda.util.mapping;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import org.apache.commons.lang3.text.StrBuilder;


public class EnumCustomConverter implements CustomConverter {

    @Override
    public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if (source == null)
            return null;
        if (destinationClass != null) {
            if ("String".equalsIgnoreCase(destinationClass.getSimpleName())) {
                return this.getString(source);
            } else if (destinationClass.isEnum()) {

                return this.getEnum(destinationClass, source);

            } else {
                throw new MappingException(new StringBuffer("Converter ").append(this.getClass().getSimpleName())
                        .append(" was used incorrectly. Arguments were: ")
                        .append(destinationClass.getClass().getName())
                        .append(" and ")
                        .append(source).toString());
            }
        }
        return null;
    }

    private Object getString(Object object) {
        String value = object.toString();
        return value;
    }

    private Object getEnum(Class<?> destinationClass, Object source) {
        Object enumeration = null;

        Method[] ms = destinationClass.getMethods();
        for (Method m : ms) {
            if ("valueOf".equalsIgnoreCase(m.getName())) {
                try {
                    enumeration = m.invoke(destinationClass.getClass(), (String) source);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return enumeration;
            }
        }
        return null;
    }
}
