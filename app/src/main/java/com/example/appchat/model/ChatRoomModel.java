package com.example.appchat.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {

    String chatRoomid;
    List<String> userId;
    Timestamp timestamp;
    String lastMessage;
    String lastMessageSend;


    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomid, List<String> userId, Timestamp timestamp, String lastMessage) {
        this.chatRoomid = chatRoomid;
        this.userId = userId;
        this.timestamp = timestamp;
        this.lastMessage = lastMessage;
    }

    public String getChatRoomid() {
        return chatRoomid;
    }

    public void setChatRoomid(String chatRoomid) {
        this.chatRoomid = chatRoomid;
    }

    public List<String> getUserid() {
        return userId;
    }

    public void setUserid(List<String> userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageSend() {
        return lastMessageSend;
    }

    public void setLastMessageSend(String lastMessageSend) {
        this.lastMessageSend = lastMessageSend;
    }


}
