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


        DbConnector dbConnector = new DbConnector("banandb","root","","localhost:3306");
        String yQuery = "add user (id=7,username='test3232@mail.ru',active=true,balance=0)";
        String yQuery1 = "add user";

        dbConnector.setQuery(yQuery,User.class);




    }
}
