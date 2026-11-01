package com.minibank.util;

import com.minibank.model.annotation.MaxLength;
import com.minibank.model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationValidator {
    private AnnotationValidator() {
    }

    public static String[] validate(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        List<String> errors = new ArrayList<>();
        for (Class<?> type = object.getClass(); type != null && type != Object.class; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    Positive positive = field.getAnnotation(Positive.class);
                    if (positive != null && value instanceof Number number && number.doubleValue() <= 0) {
                        errors.add(field.getName() + ": " + positive.message());
                    }
                    MaxLength maxLength = field.getAnnotation(MaxLength.class);
                    if (maxLength != null && value instanceof String text && text.length() > maxLength.value()) {
                        errors.add(field.getName() + ": length must be at most " + maxLength.value());
                    }
                } catch (IllegalAccessException exception) {
                    errors.add(field.getName() + ": could not be inspected");
                }
            }
        }
        return errors.toArray(String[]::new);
    }
}