package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.annotation.WebClass;
import com.winter.context.dbLanguage.DbTool;
import com.winter.context.vertx.WebVertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import lombok.Data;

import java.util.List;

@Data
@Bean
@WebClass
public class MyClass {

    public void go() {
        DbTool dbTool = new DbTool("vertxdb", "root", "", "localhost:3306");
        WebVertx webVertx = WebVertx.getWebVertx(8080, MyClass.class);
        Router router = webVertx.router;

        webVertx.createRedirect("/", "get", "/home");
        webVertx.createRedirect("/home", "get", "/login");
        webVertx.createEndPoint("/info", "get", "Library for Vertx");


        router.get("/getUsers").handler(routingContext -> {
            try {
                List<User> users = (List<User>) dbTool.execute("native select * from user", User.class);
                if (users != null) {
                    routingContext.end(Json.encodeToBuffer(users));
                } else {
                    routingContext.end(Json.encodeToBuffer("Not found"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        router.get("/getUser").handler(routingContext -> {
            String username = routingContext.request().getParam("username");
            User user = null;
            try {
                user = dbTool.execute("get user (username='" + username + "')", User.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (user != null) {
                routingContext.end(Json.encodeToBuffer(user));
            } else {
                routingContext.end(Json.encodeToBuffer("Not found"));
            }
        });
        router.get("/deleteUser").handler(routingContext -> {
            String username = routingContext.request().getParam("username");
            try {
                boolean isDeleted = dbTool.execute("delete user (username='" + username + "')", Boolean.class);
                if (isDeleted) {
                    routingContext.end(Json.encodeToBuffer("Deleted"));
                } else {
                    routingContext.end(Json.encodeToBuffer("Error"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        router.get("/addUser").handler(routingContext -> {
            String username = routingContext.request().getParam("username");
            String password = routingContext.request().getParam("password");
            long id = Long.valueOf(routingContext.request().getParam("id"));
            try {
                boolean isDeleted = dbTool.execute("add user (id='" + id + "',username='" + username + "',password='" + password + "')", Boolean.class);
                if (isDeleted) {
                    routingContext.end(Json.encodeToBuffer("Added"));
                } else {
                    routingContext.end(Json.encodeToBuffer("Error"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        router.get("/updateUser").handler(routingContext -> {
            String username = routingContext.request().getParam("username");
            long id = Long.valueOf(routingContext.request().getParam("id"));

            try {
                boolean isDeleted = dbTool.execute("update user id=" + id + " (username='" + username + "')", Boolean.class);
                if (isDeleted) {
                    routingContext.end(Json.encodeToBuffer("Updated"));
                } else {
                    routingContext.end(Json.encodeToBuffer("Error"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        webVertx.start();
    }
}