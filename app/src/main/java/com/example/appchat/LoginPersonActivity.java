package com.example.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appchat.databinding.ActivityLoginPersonBinding;
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginPersonActivity extends AppCompatActivity {

    private ActivityLoginPersonBinding binding;

    String numeroTelefono;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPersonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        numeroTelefono = getIntent().getExtras().getString("phone");
        getUsername();

        binding.btnSeguir.setOnClickListener((v) -> {
            setUsername();
        });


    }

    private void setUsername() {

        String username = binding.editUsername.getText().toString();
        if (username.isEmpty() || username.length() < 3) {
            binding.editUsername.setError("Nombre invalido");
            return;

        }
        setinProgress(true);
        if (userModel != null) {
            userModel.setUsername(username);
        } else {
            userModel = new UserModel(username, numeroTelefono, Timestamp.now(),FirebaseUtil.currentUser());
        }

        FirebaseUtil.userDocumentReference().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setinProgress(false);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginPersonActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }


    private void getUsername() {
        setinProgress(true);
        FirebaseUtil.userDocumentReference().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                setinProgress(false);
                if (task.isSuccessful()) {
                    userModel = task.getResult().toObject(UserModel.class);

                    if (userModel != null) {

                        binding.editUsername.setText(userModel.getUsername());
                    }

                }
            }
        });


    }

    void setinProgress(boolean inProgress) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSeguir.setVisibility(View.GONE);

        } else {

            binding.progressBar.setVisibility(View.GONE);
            binding.btnSeguir.setVisibility(View.VISIBLE);
        }

    }
}