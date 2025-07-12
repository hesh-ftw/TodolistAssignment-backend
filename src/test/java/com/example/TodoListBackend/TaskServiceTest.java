package com.example.TodoListBackend;

import com.example.TodoListBackend.Model.Task;
import com.example.TodoListBackend.Model.User;
import com.example.TodoListBackend.Repository.TaskRepository;
import com.example.TodoListBackend.Repository.UserRepository;
import com.example.TodoListBackend.RequestDtos.TaskRequest;
import com.example.TodoListBackend.Service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_ShouldSaveTask() {
        User user = new User();
        user.setId(1L);

        TaskRequest request = new TaskRequest();
        request.setDescription("Test Task");
        request.setDeadLine(LocalDateTime.now().plusDays(1));

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setUser(user);
        savedTask.setDescription(request.getDescription());
        savedTask.setDeadLine(request.getDeadLine());

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        Task result = taskService.createTask(request, user);

        assertNotNull(result);
        assertEquals("Test Task", result.getDescription());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldUpdateFields() {
        Long taskId = 1L;
        TaskRequest update = new TaskRequest();
        update.setDescription("Updated Task");
        update.setDeadLine(LocalDateTime.now().plusDays(2));

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setDescription("Old Task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task updatedTask = taskService.updateTask(taskId, update);

        assertEquals("Updated Task", updatedTask.getDescription());
        verify(taskRepository).save(existingTask);
    }

    @Test
    void deleteTask_ShouldRemoveTask() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void viewAllTasks_ShouldReturnUserTasks() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<Task> tasks = Arrays.asList(new Task(), new Task());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByUserId(userId)).thenReturn(tasks);

        List<Task> result = taskService.viewAllTasks(userId);

        assertEquals(2, result.size());
    }

    @Test
    void viewTodaysTasks_ShouldReturnTasksForToday() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<Task> tasks = Collections.singletonList(new Task());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findByUserIdAndDeadLineBetween(anyLong(), any(), any())).thenReturn(tasks);

        List<Task> result = taskService.viewTodaysTasks(userId);

        assertEquals(1, result.size());
    }
}
