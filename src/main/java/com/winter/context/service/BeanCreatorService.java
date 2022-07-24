package com.winter.context.service;


import com.winter.context.Context;
import com.winter.context.annotation.Bean;
import com.winter.context.util.AnnotationService;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;

public class BeanCreatorService {

    private static HashMap<String, Object> beans = new HashMap<>();
    private static String scope = "SINGLETON";
    private static int index = 1;


    static void createBean(String path) throws Exception {
        if (!Context.isRunned) {
            System.out.println("Winter is not runned");
            return;
        }

        Set<Class<?>> classes = AnnotationService.getAnnotatedClasses(Bean.class, path);
        for (Class c :
                classes) {
            if (scope.equals("SINGLETON")) {
                if (beans.containsKey(c.getName())) {
                    return;
                } else {
                    Class<?> clazz = Class.forName(c.getName());
                    Constructor<?> ctor = clazz.getConstructor();
                    Object object = ctor.newInstance();
                    String[] s = c.getSimpleName().split("");
                    s[0] = s[0].toLowerCase();
                    String beanName = "";
                    for (int i = 0; i < s.length; i++) {
                        beanName = beanName + s[i];
                    }
                    beans.put(beanName, object);
                }
            }
        }
    }


    static <T> T getBean(Class<T> c) throws Exception {
        if (!Context.isRunned) {
            System.out.println("Winter is not runned");
            return null;
        }

        String[] s = c.getSimpleName().split("");
        s[0] = s[0].toLowerCase();
        String beanName = "";
        for (int i = 0; i < s.length; i++) {
            beanName = beanName + s[i];
        }
        if (!beans.containsKey(beanName)) {
            return null;
        } else return (T) beans.get(beanName);
    }

}
