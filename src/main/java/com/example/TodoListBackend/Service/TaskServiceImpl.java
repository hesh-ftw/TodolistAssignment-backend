package com.example.TodoListBackend.Service;

import com.example.TodoListBackend.Model.Task;
import com.example.TodoListBackend.Model.User;
import com.example.TodoListBackend.Repository.TaskRepository;
import com.example.TodoListBackend.Repository.UserRepository;
import com.example.TodoListBackend.RequestDtos.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    //--------implementations of
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Task createTask(TaskRequest task, User user) {

        Task newTask= new Task();
        newTask.setUser(user);
        newTask.setDescription(task.getDescription());
        newTask.setDeadLine(task.getDeadLine());

        Task savedTask= taskRepository.save(newTask);

        return savedTask;
    }

    @Override
    public Task updateTask(Long taskId, TaskRequest updateTask) {

        Task task= taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task Not Found !"));

        if(updateTask.getDescription() != null) {
            task.setDescription(updateTask.getDescription());
        }
        if(updateTask.getDeadLine() != null) {
            task.setDeadLine(updateTask.getDeadLine());
        }

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task= taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task not found !"));

        taskRepository.delete(task);
    }

    @Override
    public List<Task> viewAllTasks(Long userId) {

        User user= userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found !"));

        List<Task> allTasks= taskRepository.findByUserId(userId);

        return allTasks;
    }

    @Override
    public List<Task> viewTodaysTasks(Long userId) {

        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        // get the list of tasks which has deadline for today
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 23:59:59

        List<Task> todayTask= taskRepository.findByUserIdAndDeadLineBetween(userId, startOfDay, endOfDay);

        return todayTask;
    }


}
