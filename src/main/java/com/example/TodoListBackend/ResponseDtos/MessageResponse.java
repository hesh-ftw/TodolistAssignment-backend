package com.example.TodoListBackend.ResponseDtos;
import lombok.Data;

@Data
public class MessageResponse {
    private String message;

    public MessageResponse(String s) {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}