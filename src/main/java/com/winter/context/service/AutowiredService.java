package com.winter.context.service;


import com.winter.context.Context;
import com.winter.context.annotation.Autowired;
import com.winter.context.util.AnnotationService;

import java.lang.reflect.Field;
import java.util.Set;

public class AutowiredService {

    static void putObjectInFields(String path) {
        if (!Context.isRunned) {
            System.out.println("Winter is not runned");
            return;
        }

        Set<Field> fields = AnnotationService.getAutowiredFields(Autowired.class, path);
        fields.stream().forEach(field -> {
            try {
                field.setAccessible(true);
                field.set(BeanCreatorService.getBean(field.getDeclaringClass()), BeanCreatorService.getBean(field.getType()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
