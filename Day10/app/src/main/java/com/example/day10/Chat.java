package com.example.day10;

public class Chat {
    public String sender;
    public String message;
    public long timestamp;

    public Chat() {
    }

    public Chat(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}
