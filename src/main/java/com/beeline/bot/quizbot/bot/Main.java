package com.beeline.bot.quizbot.bot;

import com.pengrad.telegrambot.request.SetWebhook;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) throws Exception {

        //ipAddress("172.28.171.230");
        //port(Integer.parseInt("6565"));

        get("/hello", (req, res) -> "Hello World");
        post("/test", new Test());
        get("/test", new Test());

        get("/kurs", (request, response) -> new KursBot().getAllKurs());
        post("/kursBot", new KursBot());

        BotHandler botHandler = new AqivnBot();
        post("/" + botHandler.getToken(), botHandler);

        String appSite = "bee.skymobile.local"; //System.getenv("OPENSHIFT_APP_DNS"); //OPENSHIFT_APP_DNS
        botHandler.getBot().execute(new SetWebhook().url(appSite + "/" + botHandler.getToken()));
    }

    private static class Test implements Route {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            return "ok, test";
        }
    }
}
