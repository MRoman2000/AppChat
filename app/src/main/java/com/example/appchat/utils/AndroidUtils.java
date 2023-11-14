package com.example.appchat.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.appchat.model.UserModel;

public class AndroidUtils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public  static  void passUserModel(Intent intent, UserModel userModel) {
        intent.putExtra("username",userModel.getUsername());
        intent.putExtra("phone",userModel.getTelefono());
        intent.putExtra("userId",userModel.getUserID());


    }

    public static UserModel getUserModel(Intent intent) {
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setTelefono(intent.getStringExtra("phone"));
        userModel.setUserID(intent.getStringExtra("userId"));
        return userModel;
    }
}
