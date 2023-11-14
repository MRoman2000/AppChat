package com.example.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.appchat.databinding.ActivityLoginPhoneBinding;
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginPhoneActivity extends AppCompatActivity {
    private ActivityLoginPhoneBinding binding;
    String numeroTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        binding = ActivityLoginPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.progressBar.setVisibility(View.GONE);
        binding.countryCode.registerCarrierNumberEditText(binding.editTelefono);


        binding.btnSeguir.setOnClickListener((v) -> {
            if (!binding.countryCode.isValidFullNumber()) {
                binding.editTelefono.setError("Numero invalido");
                return;

            }
            Intent intent = new Intent(this, LoginOtpActivity.class);
            intent.putExtra("phone", binding.countryCode.getFullNumberWithPlus());
            startActivity(intent);




        });

    }
}