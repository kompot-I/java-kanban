package com.yandex.app.server;

import com.google.gson.Gson;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.ManagerFactory;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTest {
    HistoryManager historyManager = ManagerFactory.getHistoryManager();
    TaskManager manager = new InMemoryTaskManager(historyManager);
    HttpTaskServer httpTaskServer = new HttpTaskServer(manager, historyManager);
    Gson gson = ManagerFactory.getGson();

    private static final String HOST = "http://localhost:8080";

    public HttpTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        httpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    public void addTask() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasks = manager.takeTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Task 1", tasks.get(0).getName());
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);
        int taskId = task.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/tasks/" + taskId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task receivedTask = gson.fromJson(response.body(), Task.class);

        assertNotNull(receivedTask);
        assertEquals(taskId, receivedTask.getId());
        assertEquals("Task 1", receivedTask.getName());
        assertEquals("Description 1", receivedTask.getDescription());
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);
        int taskId = task.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/tasks/" + taskId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.takeTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    public void getTaskWithInvalidId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/tasks/770");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Задача не найдена", response.body());
    }

    @Test
    public void detTaskWithInvalidPath() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/tasks/invalid");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Неправильный параметр строки", response.body());
    }

    @Test
    public void deleteTaskWithInvalidId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/tasks/770");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Задача не найдена", response.body());
    }

    @Test
    public void deleteTaskWithInvalidPath() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/tasks/invalid");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Неправильный параметр строки", response.body());
    }

    @Test
    public void addEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> epics = manager.takeEpicTasks();

        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals("Epic 1", epics.get(0).getName());
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        manager.addEpic(epic);
        int epicId = epic.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/" + epicId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Epic receivedEpic = gson.fromJson(response.body(), Epic.class);

        assertNotNull(receivedEpic);
        assertEquals(epicId, receivedEpic.getId());
        assertEquals("Epic 1", receivedEpic.getName());
        assertEquals("Epic Description 1", receivedEpic.getDescription());
    }

    @Test
    public void getEpicWithTaskAndSubtaskId() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description 1", epic.getId(), LocalDateTime.now(), 60);
        manager.addSubTask(subtask);
        Task task = new Task("Task 1", "Task Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/" + task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Эпик не найден", response.body());

        url = URI.create(HOST + "/epics/" + subtask.getId());

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Эпик не найден", response.body());
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        manager.addEpic(epic);
        int epicId = epic.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/" + epicId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> epics = manager.takeEpicTasks();
        assertEquals(0, epics.size());
    }

    @Test
    public void getEpicWithInvalidId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/770");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Эпик не найден", response.body());
    }

    @Test
    public void deleteEpicWithInvalidId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/770");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Эпик не найден", response.body());
    }

    @Test
    public void getEpicWithInvalidPath() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/invalid");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Неправильный параметр строки", response.body());
    }

    @Test
    public void deleteEpicWithInvalidPath() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/invalid");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Неправильный параметр строки", response.body());
    }

    @Test
    public void addSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        manager.addEpic(epic);
        int epicId = epic.getId();

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description 1", epicId, LocalDateTime.now(), 60);
        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> subtasks = manager.takeSubtasks();

        assertNotNull(subtasks);
        assertEquals(1, subtasks.size());
        assertEquals("Subtask 1", subtasks.get(0).getName());
    }

    @Test
    public void getSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        manager.addEpic(epic);
        int epicId = epic.getId();

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description 1", epicId, LocalDateTime.now(), 60);
        manager.addSubTask(subtask);
        int subtaskId = subtask.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/subtasks/" + subtaskId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Subtask receivedSubtask = gson.fromJson(response.body(), Subtask.class);

        assertNotNull(receivedSubtask);
        assertEquals(subtaskId, receivedSubtask.getId());
        assertEquals("Subtask 1", receivedSubtask.getName());
        assertEquals("Subtask Description 1", receivedSubtask.getDescription());
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        manager.addEpic(epic);
        int epicId = epic.getId();

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description 1", epicId, LocalDateTime.now(), 60);
        manager.addSubTask(subtask);
        int subtaskId = subtask.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/subtasks/" + subtaskId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> subtasks = manager.takeSubtasks();
        assertEquals(0, subtasks.size());
    }
//todo
    @Test
    public void getSubtaskWithInvalidId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/subtasks/770");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Сабтаск не найден", response.body());
    }

    @Test
    public void testGetSubtaskWithInvalidPath() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/subtasks/invalid");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Неправильный параметр строки", response.body());
    }

    @Test
    public void testDeleteSubtaskWithInvalidId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/subtasks/770");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Сабтаск не найден", response.body());
    }

    @Test
    public void testDeleteSubtaskWithInvalidPath() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epics/subtasks/invalid");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Неправильный параметр строки", response.body());
    }
    //todo
    @Test
    public void getSubtasksOfEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        manager.addEpic(epic);
        int epicId = epic.getId();

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", epicId, LocalDateTime.now(), 60);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", epicId, LocalDateTime.now().plusHours(1), 60);
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/epic/subtasks/" + epicId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Subtask[] receivedSubtasks = gson.fromJson(response.body(), Subtask[].class);

        assertNotNull(receivedSubtasks);
        assertEquals(2, receivedSubtasks.length);
        assertEquals("Subtask 1", receivedSubtasks[0].getName());
        assertEquals("Subtask 2", receivedSubtasks[1].getName());
    }
}


