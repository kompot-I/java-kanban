package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.util.Arrays;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
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
        String[] parts = exchange.getRequestURI().getPath().split("/");

        if (parts.length == 3) {
            try {
                int id = Integer.parseInt(parts[2]);
                if (taskManager.getTaskByID(id) == null) {
                    sendText(exchange, "Задача не найдена", 404);
                    return;
                }

                response = gson.toJson(taskManager.getTaskByID(id));
                sendText(exchange, response, 200);
            } catch (NumberFormatException e) {
                sendText(exchange, "Неправильный параметр строки", 400);
            }
        }
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        String requestBody = readText(exchange);
        Task task = gson.fromJson(requestBody, Task.class);

        if (query == null) {
            taskManager.addTask(task);

            if (taskManager.getPrioritizedTasks().contains(task)) {
                sendText(exchange, "Задача добавлена", 201);
            } else {
                System.out.println("tyt");
                sendText(exchange, "Задача не добавлена: пересечение по времени", 406);
            }
            return;
        }

        try {
            taskManager.updateTask(task);
            sendText(exchange, "Задача обновлена", 201);
        } catch (NumberFormatException e) {
            sendText(exchange, "Неправильный параметр строки", 400);
        }
    }

    public void handleDelete(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");

        if (parts.length == 2) {
            taskManager.deleteTasks();
            sendText(exchange, "Задачи успешно удалены", 200);
            return;
        }

        if (parts.length == 3) {
            try {
                int id = Integer.parseInt(parts[2]);
                taskManager.deleteTaskByID(id);
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
