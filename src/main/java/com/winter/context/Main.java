package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.dbLanguage.DbConnector;
import com.winter.context.generatedClasses.User;
import com.winter.context.util.ClassGenerator;


@Bean
public class Main {

    public static void main(String[] args) throws Exception {

        Context context = Context.getContext("com");
        context.start();
        ClassGenerator classGenerator = new ClassGenerator("com.winter.context", "com.winter.context.generatedClasses");
        // classGenerator.generateClassFromXProtocol("User","username:String/password:String/active:boolean/activation_code:String/balance:long/id:long");

        DbConnector dbConnector = new DbConnector("banandb", "root", "", "localhost:3306");
        String y = "get user (id=95)";
        String x = "delete user (id=101)";
        User user = new User();
        user.setUsername("dav@mail.ru");
        System.out.println(dbConnector.execute(x, User.class));







    }
}
