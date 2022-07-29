package com.winter.context.broker;

import com.winter.context.Context;
import com.winter.context.vertx.WebVertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;


public class Broker extends AbstractVerticle {

    private Vertx vertx = Vertx.vertx();
    private HttpServer server = vertx.createHttpServer();
    private Router router = Router.router(vertx);
    private int port = 8080;
    public HashMap<String, String> subscribedMicroservices = new HashMap<>();


    @Override
    public void start() throws Exception {
        if (!Context.isRunned) {
            return;
        }
        router.get("/subscribe").handler(routingContext -> {
            String microserviceName = routingContext.request().getParam("name");
            String checkPath = routingContext.request().getParam("path");
            if ((microserviceName != null) && (checkPath != null)) {
                subscribedMicroservices.put(microserviceName, checkPath);
                System.out.println(microserviceName + " subscribed");
                routingContext.response().setStatusCode(200).end("Subscribed");
            } else {
                routingContext.end("Invalid params");
            }

        });

        router.get("/to").handler(routingContext -> {
            String path = routingContext.request().getParam("path");
            String from = routingContext.request().getParam("from");
            String to = routingContext.request().getParam("to");
            if (!(subscribedMicroservices.containsKey(from))&&(subscribedMicroservices.containsKey(to))) {
                routingContext.end("Not subscribed");
            }

            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(URI.create("http://" + to + "/" + path))
                    .header("accept", "application/json")
                    .build();
            try {
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                routingContext.response().putHeader("content-type", "application/json");
                routingContext.end(Json.encodeToBuffer(response.body()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        server.requestHandler(router);
        server.listen(port);
    }
}
