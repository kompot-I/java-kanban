package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.util.Arrays;

public class SubtaskHandler extends BaseHttpHandler {
    public SubtaskHandler(TaskManager taskManager) {
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

        if (parts.length == 4) {
            try {
                int id = Integer.parseInt(parts[3]);
                Subtask subtask = taskManager.getSubtaskByID(id);
                if (subtask == null) {
                    sendText(exchange, "Сабтаск не найден", 404);
                    return;
                }

                response = gson.toJson(subtask);
                sendText(exchange, response, 200);
            } catch (NumberFormatException e) {
                sendText(exchange, "Неправильный параметр строки", 400);
            }
        } else if (query == null) {
            response = gson.toJson(taskManager.takeSubtasks());
            sendText(exchange, response, 200);
        }
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        String requestBody = readText(exchange);
        Subtask subTask = gson.fromJson(requestBody, Subtask.class);

        if (query == null) {
            taskManager.addSubTask(subTask);
            if (taskManager.getPrioritizedTasks().contains(subTask)) {
                sendText(exchange, "Задача добавлена", 201);
            } else {
                sendText(exchange, "Задача не добавлена: пересечение по времени", 406);
            }
            return;
        }

        try {
            taskManager.updateTask(subTask);
            sendText(exchange, "Задача обновлена", 201);
        } catch (NumberFormatException e) {
            sendText(exchange, "Неправильный параметр строки", 400);
        }
    }

    public void handleDelete(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");

        if (parts.length == 3) {
            taskManager.deleteSubtasks();
            sendText(exchange, "Задачи успешно удалены", 200);
            return;
        }

        if (parts.length == 4) {
            try {
                int id = Integer.parseInt(parts[3]);
                taskManager.deleteSubtaskById(id);
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
