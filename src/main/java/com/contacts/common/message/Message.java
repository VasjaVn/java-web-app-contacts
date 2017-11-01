package com.contacts.common.message;

public class Message {
    private String type;
    private String message;

    public Message() {}

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}