package com.telus.credit.crda.util.mapping;

import org.dozer.CustomConverter;

public class EnumConverter implements CustomConverter {

    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                          Class<?> destinationClass, Class<?> sourceClass) {

        if (!destinationClass.isEnum()) {
            throw new IllegalArgumentException("destinationClass must be an enum");
        }
        if (!sourceClass.isEnum()) {
            throw new IllegalArgumentException("sourceClass must be an enum");
        }

        Enum<?> sourceValue = (Enum<?>) sourceFieldValue;
        Object[] values = destinationClass.getEnumConstants();
        for (Object value : values) {
            Enum<?> enumValue = (Enum<?>) value;
            if (enumValue.name().equals(sourceValue.name())) {

                return enumValue;
            }
        }
        throw new IllegalArgumentException("Enum constant [" + sourceValue + "] not found in "
                + destinationClass);
    }

}


