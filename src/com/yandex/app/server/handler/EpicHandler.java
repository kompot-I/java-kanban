package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Epic;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.util.Arrays;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
            default:
                sendText(exchange, "Неправильный запрос", 400);
        }
    }

    public void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String[] parts = exchange.getRequestURI().getPath().split("/");

        if (parts.length == 3) {
            try {
                int id = Integer.parseInt(parts[2]);

                Epic epic = taskManager.getEpicByID(id);
                if (epic == null) {
                    sendText(exchange, "Эпик не найден", 404);
                    return;
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
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String requestBody = readText(exchange);
        Epic epic = gson.fromJson(requestBody, Epic.class);

        if (query == null) {
            taskManager.addEpic(epic);
            sendText(exchange, "Задача добавлена", 201);
        } else {
            try {
                taskManager.updateTask(epic);
                sendText(exchange, "Задача обновлена", 201);
            } catch (NumberFormatException e) {
                sendText(exchange, "Неправильный параметр строки", 400);
            }
        }
    }

    public void handleDelete(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");

        if (parts.length == 2) {
            taskManager.deleteEpics();
            sendText(exchange, "Задачи успешно удалены", 200);
            return;
        }

        if (parts.length == 3) {
            try {
                int id = Integer.parseInt(parts[2]);
                taskManager.deleteEpicTasksById(id);
                sendText(exchange, "Задача успешно удалена", 200);
            } catch (NumberFormatException e) {
                sendText(exchange, "Неправильный параметр строки", 400);
            } catch (IllegalArgumentException e) {
                sendText(exchange, e.getMessage(), 404);
            }
        } else {
            sendText(exchange, "Неправильный запрос", 400);
        }
    }
}
