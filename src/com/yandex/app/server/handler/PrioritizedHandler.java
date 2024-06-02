package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            response = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(exchange, response, 200);
        } else {
            sendText(exchange, "Неправильный запрос", 400);
        }
    }
}
