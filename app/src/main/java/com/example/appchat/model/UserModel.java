package com.example.appchat.model;

import com.google.firebase.Timestamp;

public class UserModel {

    private String username;
    private String telefono;
    private Timestamp timestamp;
    private String userID;

    public UserModel() {
    }

    public UserModel(String username, String telefono, Timestamp timestamp,String userID) {
        this.username = username;
        this.telefono = telefono;
        this.timestamp = timestamp;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
