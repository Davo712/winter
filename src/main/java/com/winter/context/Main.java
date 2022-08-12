package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.dbLanguage.DbConnector;
import com.winter.context.generatedClasses.User;
import com.winter.context.service.BeanCreatorService;
import com.winter.context.util.ClassGenerator;

import java.util.Optional;


@Bean
public class Main {

    public static void main(String[] args) throws Exception {

        Context context = Context.getContext("com", new ClassGenerator("com.winter.context", "com.winter.context.generatedClasses"));
        context.start();


        DbConnector dbConnector = new DbConnector("banandb", "root", "", "localhost:3306");
        String y = "get user (id=95)";
        String x = "get user";


        User user = new User();
        user.setUsername("dav@mail.ru");


       User user1 = dbConnector.setQuery(x,User.class,user);
        System.out.println(user1);



    }
}
