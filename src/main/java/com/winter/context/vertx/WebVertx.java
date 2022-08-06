package com.winter.context.vertx;

import com.winter.context.Context;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;

import java.util.List;


public class WebVertx extends AbstractVerticle {
    private Vertx vertx = Vertx.vertx();
    private HttpServer server = vertx.createHttpServer();
    private Router router = Router.router(vertx);

    public int port;
    public Class aClass;

    public WebVertx(int port, Class aClass) {
        this.port = port;
        this.aClass = aClass;
    }

    public static WebVertx getWebVertx(int port, Class aClass) {
        if (!Context.isRunned) {
            return null;
        }
        WebVertx webVertx = new WebVertx(port, aClass);
        return webVertx;
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

    /// -----------------------------------------------------

    public void createEndPoint(String endPoint, String httpMethodType, Object response) {
        if (!Context.isRunned) {
            return;
        }
        if (httpMethodType.equals("get")) {
            router.get(endPoint).handler(routingContext -> {
                HttpServerResponse rsp = routingContext.response();
                rsp.putHeader("content-type", "application/json");
                rsp.end(Json.encodeToBuffer(response));
            });
        } else if (httpMethodType.equals("post")) {
            router.post(endPoint).handler(routingContext -> {
                HttpServerResponse rsp = routingContext.response();
                rsp.putHeader("content-type", "application/json");
                rsp.end(Json.encodeToBuffer(response));
            });
        }
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

    public void createRedirect(String endPoint, String httpMethodType, String redirectTo) {
        if (!Context.isRunned) {
            return;
        }
        if (httpMethodType.equals("get")) {
            router.get(endPoint).handler(routingContext -> routingContext.redirect(redirectTo));
        } else if (httpMethodType.equals("post")) {
            router.post(endPoint).handler(routingContext -> routingContext.redirect(redirectTo));
        }
    }

    public void createEndPointWithParams(String endPoint, String httpMethodType, List<String> paramNames, String response) {
        if (!Context.isRunned) {
            return;
        }

        if (httpMethodType.equals("get")) {
            router.get(endPoint).handler(routingContext -> {
                for (int i = 0; i < paramNames.size(); i++) {
                    System.out.println(routingContext.request().getParam(paramNames.get(i)));
                }
                routingContext.end(response);
            });
        } else if (httpMethodType.equals("post")) {
            router.post(endPoint).handler(routingContext -> {
                for (int i = 0; i < paramNames.size(); i++) {
                    System.out.println(routingContext.request().getParam(paramNames.get(i)));
                }
                routingContext.end(response);
            });
        }
    }

}
