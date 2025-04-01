package com.example.TodoListBackend.Service;

import com.example.TodoListBackend.Model.User;

public interface UserService {
    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByEmail(String email) throws Exception;

    public void generatePasswordResetToken(String email);

    public void resetPassword(String token, String newPassword);
}

