package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.service.HistoryManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(HistoryManager historyManager) {
        super(historyManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            response = gson.toJson(historyManager.getHistory());
            sendText(exchange, response, 200);
        } else {
            sendText(exchange, "Неправильный запрос", 400);
        }
    }
}
