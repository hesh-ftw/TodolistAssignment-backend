package com.example.TodoListBackend.Controller;

import com.example.TodoListBackend.Model.Task;
import com.example.TodoListBackend.Model.User;
import com.example.TodoListBackend.RequestDtos.TaskRequest;
import com.example.TodoListBackend.ResponseDtos.MessageResponse;
import com.example.TodoListBackend.Service.TaskService;
import com.example.TodoListBackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    //-------------API endpoints of TASKS-----------------

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestHeader("Authorization") String jwt,
                                           @RequestBody TaskRequest taskRequest) throws Exception {

        User user= userService.findUserByJwtToken(jwt);
        Task task= taskService.createTask(taskRequest, user);

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@RequestHeader("Authorization") String jwt,
                                           @RequestBody TaskRequest taskRequest,
                                           @PathVariable Long taskId) throws Exception {

        User user= userService.findUserByJwtToken(jwt);
        Task task= taskService.updateTask(taskId,taskRequest);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<MessageResponse> deleteTask(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable Long taskId) throws Exception {

        User user= userService.findUserByJwtToken(jwt);
        taskService.deleteTask(taskId);

        MessageResponse response= new MessageResponse();
        response.setMessage("task deleted success!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //return all the task of the authenticated user
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks(@RequestHeader("Authorization") String jwt) throws Exception {

        User user= userService.findUserByJwtToken(jwt);

        List<Task> task= taskService.viewAllTasks(user.getId());

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    //return all the tasks that will due today
    @GetMapping("/all-today")
    public ResponseEntity<List<Task>> getTodaysTasks(@RequestHeader("Authorization") String jwt) throws Exception {

        User user= userService.findUserByJwtToken(jwt);

        List<Task> task= taskService.viewTodaysTasks(user.getId());

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

}
