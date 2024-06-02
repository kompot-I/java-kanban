package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
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
                break;

            case "POST":
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
                    break;
                }

                try {
                    taskManager.updateTask(task);
                    sendText(exchange, "Задача обновлена", 201);
                } catch (NumberFormatException e) {
                    sendText(exchange, "Неправильный параметр строки", 400);
                }
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.deleteTasks();
                    sendText(exchange, "Задачи успешно удалены", 200);
                    break;
                }

                try {
                    int id = Integer.parseInt(parts[2]);
                    taskManager.deleteTaskByID(id);
                    sendText(exchange, "Задача успешно удалена", 200);
                } catch (NumberFormatException e) {
                    sendText(exchange, "Неправильный параметр строки", 400);
                }
                break;

            default:
                sendText(exchange, "Неправильный запрос", 400);
        }
    }
}
