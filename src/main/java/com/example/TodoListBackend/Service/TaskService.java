package com.example.TodoListBackend.Service;

import com.example.TodoListBackend.Model.Task;
import com.example.TodoListBackend.Model.User;
import com.example.TodoListBackend.RequestDtos.TaskRequest;

import java.util.List;

public interface TaskService {

    public Task createTask(TaskRequest task, User user);
    public Task updateTask(Long taskId, TaskRequest updateTask);
    public void deleteTask(Long taskId);
    public List<Task> viewAllTasks(Long userId);
    public List<Task> viewTodaysTasks(Long userId);
}
