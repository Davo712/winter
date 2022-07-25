package com.winter.context.vertx;

import com.winter.context.Context;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;


public class Web extends AbstractVerticle {
    private Vertx vertx = Vertx.vertx();
    private HttpServer server = vertx.createHttpServer();
    private Router router = Router.router(vertx);

    public int port;

    private Web(int port) {
        this.port = port;
    }

    public static Web getWeb(int port) {
        Web web = new Web(port);
        return web;
    }


    public void createEndPoint(String endPoint, String httpMethodType, String response) {
        if (!Context.isRunned) {
            return;
        }
        if (httpMethodType.equals("get")) {
            router.get(endPoint).handler(routingContext -> {
               routingContext.end(response);
            });
        } else if (httpMethodType.equals("post")) {
            router.post(endPoint).handler(routingContext -> {
                routingContext.end(response);
            });
        }
    }

    public void run() {
        if (!Context.isRunned) {
            return;
        }
        vertx.deployVerticle(this);
        System.out.println("Run!");
    }

    @Override
    public void start() throws Exception {
        if (!Context.isRunned) {
            return;
        }
        server.requestHandler(router);
        server.listen(port);
    }

}
