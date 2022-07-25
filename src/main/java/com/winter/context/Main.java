package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.generatedClasses.User;
import com.winter.context.util.ClassGenerator;
import com.winter.context.vertx.Web;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;


@Bean
public class Main {


    public static void main(String[] args) throws Exception {
        Context context = Context.getContext("com", new ClassGenerator("com.winter.context", "com.winter.context.generatedClasses"));
        context.start();
        User user = new User();
        user.setId(1);
        user.setUsername("Dav");
        Web web = Web.getWeb(8080, Main.class);
        web.createEndPoint("/test", "get", user);
        List<String> paramNames = new ArrayList<>();
        paramNames.add("username");
        paramNames.add("password");
        web.createEndPointWithParams("/login", "get", paramNames, "ok");
        web.run();



    }
}
