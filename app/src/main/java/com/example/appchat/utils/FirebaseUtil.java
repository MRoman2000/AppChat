package com.example.appchat.utils;

import android.annotation.SuppressLint;

import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {
    public static String currentUser() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isAuthenticated() {
        return currentUser() != null;
    }

    public static DocumentReference userDocumentReference() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUser());

    }

    public static CollectionReference allusers() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatRoomReference(String chatroom) {
        return FirebaseFirestore.getInstance().collection("chatrooms")
                .document(chatroom);

    }

    public static String getChatroomId(String userId1, String userId2) {
        if (userId1.hashCode() < userId2.hashCode()) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

    public static CollectionReference getChatMessageReference(String charoom) {
        return getChatRoomReference(charoom).collection("chats");
    }

    public static CollectionReference allChatConnections() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtrosUser(List<String> userId) {


        if (userId.get(0).equals(FirebaseUtil.currentUser())) {
            return allusers().document(userId.get(1));
        } else {
            return allusers().document(userId.get(0));
        }
    }


    public static String timestampToString(Timestamp timestamp) {

        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfileStorage() {
        return FirebaseStorage.getInstance().getReference().child("profile_file").child(FirebaseUtil.currentUser());


    }

    public static StorageReference getOtherProfileStorage(String otros) {
        return FirebaseStorage.getInstance().getReference().child("profile_file").child(otros);


    }
}

