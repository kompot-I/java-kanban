package com.yandex.app.server;

import com.sun.net.httpserver.HttpServer;
import com.yandex.app.server.handler.*;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final HistoryManager historyManager;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager, HistoryManager historyManager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.historyManager = historyManager;
        this.taskManager = taskManager;

        httpServer.createContext("/tasks", new TaskHandler(this.taskManager));
        httpServer.createContext("/epics/subtasks", new SubtaskHandler(this.taskManager));
        httpServer.createContext("/epics", new EpicHandler(this.taskManager));
        httpServer.createContext("/history", new HistoryHandler(this.historyManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(this.taskManager));
        httpServer.createContext("/epic/subtasks", new SubtaskOfEpicHandler(this.taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }
}
