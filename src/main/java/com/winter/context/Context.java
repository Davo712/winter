package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.service.AutowiredService;
import com.winter.context.service.BeanCreatorService;
import com.winter.context.classGenerator.ClassGeneratorFromX;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Data
@Bean
public class Context {

    private Context() {
    }

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private ClassGeneratorFromX classGenerator;

    public String scanPath;

    public static boolean isRunned;

    public static Context getContext(String scanPath) {
        Context context = new Context();
        context.scanPath = scanPath;
        return context;
    }
    public static Context getContext(String scanPath, ClassGeneratorFromX classGenerator) {
        Context context = new Context();
        context.scanPath = scanPath;
        context.classGenerator = classGenerator;
        return context;
    }

    public void start() throws Exception {
        System.out.println("Context started");
        isRunned = true;

        Class<?> clazz = Class.forName(BeanCreatorService.class.getName());
        Object obj = clazz.getConstructor();
        Method method = clazz.getDeclaredMethod("createBean", String.class);
        method.setAccessible(true);
        method.invoke(obj, scanPath);

        Class<?> clazz1 = Class.forName(AutowiredService.class.getName());
        Object obj1 = clazz1.getConstructor();
        Method method1 = clazz1.getDeclaredMethod("putObjectInFields", String.class);
        method1.setAccessible(true);
        method1.invoke(obj1, scanPath);

    }

    public <T> T getBean(Class<T> c) throws Exception {
        if (!isRunned) {
            System.out.println("Winter is not runned");
            return null;
        }
        Class<?> clazz = Class.forName(BeanCreatorService.class.getName());
        Object obj = clazz.getConstructor();
        Method method = clazz.getDeclaredMethod("getBean", Class.class);
        method.setAccessible(true);
        return (T) method.invoke(obj, c);
    }
}
