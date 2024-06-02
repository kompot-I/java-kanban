package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Epic;
import com.yandex.app.service.TaskManager;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        String[] parts = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":
                if (parts.length == 3) {
                    try {
                        int id = Integer.parseInt(parts[2]);

                        Epic epic = (Epic) taskManager.getTaskByID(id);
                        if (epic == null) {
                            sendText(exchange, "Эпик не найден", 404);
                            break;
                        }

                        response = gson.toJson(epic);
                        sendText(exchange, response, 200);
                    } catch (NumberFormatException e) {
                        sendText(exchange, "Неправильный параметр строки", 400);
                    }
                } else if (query == null) {
                    response = gson.toJson(taskManager.takeEpicTasks());
                    sendText(exchange, response, 200);
                }
                break;

            case "POST":
                String requestBody = readText(exchange);
                Epic epic = gson.fromJson(requestBody, Epic.class);
                taskManager.addEpic(epic);
                sendText(exchange, "Задача добавлена", 201);
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.deleteEpics();
                    sendText(exchange, "Задачи успешно удалены", 200);
                    break;
                }

                try {
                    int id = Integer.parseInt(parts[2]);
                    taskManager.deleteEpicTasksById(id);
                    sendText(exchange, "Задача успешно удалена", 200);
                } catch (NumberFormatException e) {
                    sendText(exchange, "Неправильный параметр строки", 400);
                }
                break;
        }
    }
}
