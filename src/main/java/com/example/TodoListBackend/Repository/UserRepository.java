package com.example.TodoListBackend.Repository;

import com.example.TodoListBackend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    public User findByEmail(String username);
}
