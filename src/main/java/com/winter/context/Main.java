package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.broker.Broker;
import com.winter.context.service.BeanCreatorService;
import com.winter.context.util.ClassGenerator;
import com.winter.context.vertx.WebVertx;


@Bean
public class Main {

    public static void main(String[] args) throws Exception {

        Context context = Context.getContext("com", new ClassGenerator("com.winter.context", "com.winter.context.generatedClasses"));
        context.start();
        Main main = BeanCreatorService.getBean(Main.class);
//        WebVertx webVertx = WebVertx.getWebVertx(8080,Main.class);
//        webVertx.createEndPoint("/","get","Hello");
//        webVertx.run();
        Broker broker = new Broker();
        broker.start();
    }
}
