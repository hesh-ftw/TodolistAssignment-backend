package com.example.TodoListBackend.Repository;

import com.example.TodoListBackend.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    public List<Task> findByUserId(Long userId);

    public List<Task> findByUserIdAndDeadLineBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // public Task findByTaskId(Long taskId);
}
