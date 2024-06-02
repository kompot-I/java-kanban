package com.yandex.app.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.TaskManager;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler {
    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        String[] parts = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":
                if (parts.length == 4) {
                    try {
                        int id = Integer.parseInt(parts[3]);
                        Subtask subtask = (Subtask) taskManager.getTaskByID(id);
                        if (subtask == null) {
                            sendText(exchange, "Сабтаск не найден", 404);
                            break;
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
                break;

            case "POST":
                String requestBody = readText(exchange);
                Subtask subTask = gson.fromJson(requestBody, Subtask.class);

                if (query == null) {
                    taskManager.addSubTask(subTask);
                    if (taskManager.getPrioritizedTasks().contains(subTask)) {
                        sendText(exchange, "Задача добавлена", 201);
                    } else {
                        sendText(exchange, "Задача не добавлена: пересечение по времени", 406);
                    }
                    break;
                }

                try {
                    taskManager.updateTask(subTask);
                    sendText(exchange, "Задача обновлена", 201);
                } catch (NumberFormatException e) {
                    sendText(exchange, "Неправильный параметр строки", 400);
                }
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.deleteSubtasks();
                    sendText(exchange, "Задачи успешно удалены", 200);
                    break;
                }

                try {
                    int id = Integer.parseInt(parts[2]);
                    taskManager.deleteSubtaskById(id);
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
