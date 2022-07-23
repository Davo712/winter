package com.winter.context.util;


import com.winter.context.annotation.Autowired;
import com.winter.context.annotation.Bean;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class AnnotationService {


    public static Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation, String path) {
        Reflections reflections = new Reflections(path);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotation, true);
        if (annotatedClasses.size() == 0) {
            return new HashSet<>();
        }
        return annotatedClasses;
    }

    public static Set<Field> getAutowiredFields(Class<? extends Annotation> annotation, String path) {
        Set<Field> fieldsSet = new HashSet<>();
        Reflections reflections = new Reflections(path);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Bean.class);
        typesAnnotatedWith.forEach(aClass -> {
            Field[] fields = aClass.getDeclaredFields();
            for (Field f :
                    fields) {
                if (f.isAnnotationPresent(Autowired.class)) {
                    fieldsSet.add(f);
                }
            }
        });
        return fieldsSet;
    }

}
