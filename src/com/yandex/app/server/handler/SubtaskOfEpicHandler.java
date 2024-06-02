package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.service.TaskManager;

import java.io.IOException;

public class SubtaskOfEpicHandler extends BaseHttpHandler {
    public SubtaskOfEpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] parts = exchange.getRequestURI().getPath().split("/");

        if (method.equals("GET")) {
            try {
                int id = Integer.parseInt(parts[3]);
                if (taskManager.getSubtaskOfEpic(id) == null) {
                    sendText(exchange, "Не удалось найти Эпик", 404);
                    return;
                }

                response = gson.toJson(taskManager.getSubtaskOfEpic(id));
                sendText(exchange, response, 200);

            } catch (NumberFormatException e) {
                sendText(exchange, "Неправильный параметр строки", 400);
            }
        } else {
            sendText(exchange, "Неправильный запрос", 400);
        }
    }
}
