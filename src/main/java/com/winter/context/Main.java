package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.classGenerator.ClassGenerator;
import com.winter.context.classGenerator.ClassGeneratorFromX;
import com.winter.context.dbLanguage.DbConnector;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@Bean
public class Main {


     static void test() {

    }
    public static void main(String[] args) throws Exception {
        Context context = Context.getContext("com");
        context.start();
       // DbConnector dbConnector = new DbConnector("banandb","root","","localhost:3306");

     //   List<User> users = (List<User>) dbConnector.execute("native select * from user",User.class);
       // System.out.println(users);

        ClassGenerator classGenerator = new ClassGenerator("com","com.winter.context");
        classGenerator.setClassName("MyClass");
        classGenerator.setSuperClass(Object.class);
        classGenerator.setImplClass(Serializable.class);
        classGenerator.addField(String.class,"username","public");
        classGenerator.addField(int.class,"id",true);
        classGenerator.addField(int.class,"age",false,"private");
        classGenerator.addField(boolean.class, "isActive");
        classGenerator.addImport("java.util.*");
        classGenerator.addImport("java.io.*");
        classGenerator.addMethod("private",String.class,"getTest","String x, Boolean y","System.out.println(x + \"  \" + y);  return x;" );

        classGenerator.generate();
    }
}
