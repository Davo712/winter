package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.util.ClassGenerator;


@Bean
public class Main {


    public static void main(String[] args) throws Exception {
        Context context = Context.getContext("com");
        context.start();
        System.out.println(context.getBean(Main.class));
        ClassGenerator classGenerator = new ClassGenerator("com","com.winter.context.generatedClasses");
      //  classGenerator.generateClassFromXProtocol("User","username:String/name:String/id:long/surname:String/active:boolean/balance:long");





    }
}
