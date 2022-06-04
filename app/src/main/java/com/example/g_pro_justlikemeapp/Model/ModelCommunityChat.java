package com.example.g_pro_justlikemeapp.Model;

public class ModelCommunityChat {
    String message,timestamp,sender,type;

    public ModelCommunityChat() {
    }

    public ModelCommunityChat(String message, String timestamp, String sender, String type) {
        this.message = message;
        this.timestamp = timestamp;
        this.sender = sender;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
