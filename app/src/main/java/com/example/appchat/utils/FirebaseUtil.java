package com.example.appchat.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {
    public static String currentUser() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isAuthenticated() {
        if (currentUser() != null) {
            return true;
        }
        return false;
    }

    public static DocumentReference documentReferenceUserID() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(currentUser());

    }

    public static CollectionReference allusers() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatRoomReference(String chatroom) {
        return FirebaseFirestore.getInstance().collection("chatrooms")
                .document(chatroom);

    }

    public static String getChatroom(String userId1, String userId2) {
        if (userId1.hashCode() < userId2.hashCode()) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

}
