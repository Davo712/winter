package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.util.ClassGenerator;
import com.winter.context.vertx.Web;


@Bean
public class Main {

    public static void main(String[] args) throws Exception {
        Context context = Context.getContext("com", new ClassGenerator("com.winter.context", "com.winter.context.generatedClasses"));
        context.start();
    }
}
