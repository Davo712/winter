package com.winter.context;

import com.winter.context.annotation.Autowired;
import com.winter.context.annotation.Bean;
import com.winter.context.classGenerator.ClassGenerator;
import com.winter.context.dbLanguage.DbTool;
import com.winter.context.vertx.WebVertx;

import java.io.Serializable;
import java.util.List;


@Bean
public class Main {

    @Autowired
    private static MyClass myClass;

    public static void main(String[] args) throws Exception {
        Context context = Context.getContext("com");
        context.start();
        myClass.go();

       /* DbTool dbConnector = new DbTool("banandb", "root", "", "localhost:3306");

        List<User> users = (List<User>) dbConnector.execute("native select * from user", User.class);
        System.out.println(users);


        ClassGenerator classGenerator = new ClassGenerator("com", "com.winter.context");
        classGenerator.setClassName("MyClass");
        classGenerator.setSuperClass(Object.class);
        classGenerator.setImplClass(Serializable.class);
        classGenerator.addField(String.class, "username", "public");
        classGenerator.addField(int.class, "id", true);
        classGenerator.addField(int.class, "age", false, "private");
        classGenerator.addField(boolean.class, "isActive");
        classGenerator.addField(List.class, "lol");
        classGenerator.addImport("java.util.*");
        classGenerator.addImport("java.io.*");
        classGenerator.addMethod("private", true, String.class, "getTest", "String x, Boolean y", "System.out.println(x + \"  \" + y);  return x;");

        classGenerator.generate();

        WebVertx webVertx = WebVertx.getWebVertx(8080, Main.class);
        webVertx.createEndPoint("/home", "get", "Haylooo");
        webVertx.createRedirect("/", "get", "/home");
        webVertx.createEndPointWithParams("/login", "get", "OK", "username", "password");

        webVertx.start();

        */



    }
}
