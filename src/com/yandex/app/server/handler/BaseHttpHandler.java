package com.yandex.app.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.ManagerFactory;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {
    protected final Gson gson = ManagerFactory.getGson();
    protected TaskManager taskManager;
    protected HistoryManager historyManager;
    protected String response;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public BaseHttpHandler(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }

    protected void sendText(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(rCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}
