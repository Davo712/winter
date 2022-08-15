package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.dbLanguage.DbConnector;
import com.winter.context.generatedClasses.User;
import com.winter.context.util.ClassGenerator;

import java.util.List;


@Bean
public class Main {

    public static void main(String[] args) throws Exception {

        Context context = Context.getContext("com");
        context.start();
        ClassGenerator classGenerator = new ClassGenerator("com.winter.context", "com.winter.context.generatedClasses");
        // classGenerator.generateClassFromXProtocol("User","username:String/password:String/active:boolean/activation_code:String/balance:long/id:long");

        DbConnector dbConnector = new DbConnector("banandb", "root", "", "localhost:3306");
        String y = "add user (id=16,active=true,balance=10)";
        String x = "get user";
        String updateQUery = "update user id=16 (activation_code='assasa',balance=100,name='duq')";
        // dbConnector.execute(y,User.class);
        // User user = dbConnector.execute(y, User.class);
        // dbConnector.execute(updateQUery,User.class);
        //  System.out.println((User) dbConnector.execute(x, User.class, user));


        System.out.println(dbConnector.execute("native update user set id = 11 where id = 10", User.class, new Object()));


    }
}
