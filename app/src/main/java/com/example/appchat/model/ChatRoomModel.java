package com.example.appchat.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {

    String chatRoomid;
    List<String> userid;
    Timestamp timestamp;
    String lastMessage;


    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomid, List<String> userid, Timestamp timestamp, String lastMessage) {
        this.chatRoomid = chatRoomid;
        this.userid = userid;
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
        return userid;
    }

    public void setUserid(List<String> userid) {
        this.userid = userid;
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
}
