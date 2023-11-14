package com.example.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appchat.databinding.ActivityLoginOtpBinding;
import com.example.appchat.databinding.ActivityLoginPhoneBinding;
import com.example.appchat.utils.AndroidUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    String numeroTelefono;
    long timeoutSecond = 60L;

    String codigoVertificacion;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private ActivityLoginOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        numeroTelefono = getIntent().getExtras().getString("phone");


        senDOtop(numeroTelefono, false);

        binding.btnSeguiente.setOnClickListener(v -> {

            String entradaOTP = binding.editTextTelefono.getText().toString();
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(codigoVertificacion, entradaOTP);
            signPhone(phoneAuthCredential);
            setinProgress(true);

        });


        binding.resentOTP.setOnClickListener(v -> {
            senDOtop(numeroTelefono,true);
        });
    }

    void senDOtop(String numeroTelefono, boolean isResent) {

        staerResendTimer();
        setinProgress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(numeroTelefono)
                .setTimeout(timeoutSecond, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signPhone(phoneAuthCredential);
                        setinProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtils.showToast(getApplicationContext(), "OTP vertificacio fallada");
                        setinProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        codigoVertificacion = s;
                        resendingToken = forceResendingToken;
                        AndroidUtils.showToast(getApplicationContext(), "OTP enviado correctamente");
                        setinProgress(false);

                    }
                });

        if (isResent) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    private void staerResendTimer() {

        binding.resentOTP.setEnabled(false);
        Timer timer  = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                timeoutSecond --;
                binding.resentOTP.setText("Resend OTP in" + timeoutSecond + "secondsd");
                if(timeoutSecond <= 0) {

                    timeoutSecond = 60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        binding.resentOTP.setEnabled(true);
                    });
                }
            }

        },0,1000);
    }

    void setinProgress(boolean inProgress) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSeguiente.setVisibility(View.GONE);

        } else {

            binding.progressBar.setVisibility(View.GONE);
            binding.btnSeguiente.setVisibility(View.VISIBLE);
        }

    }

    void signPhone(PhoneAuthCredential phoneAuthCredential) {
        setinProgress(true);
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setinProgress(false);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginOtpActivity.this, LoginPersonActivity.class);
                    intent.putExtra("phone", numeroTelefono);
                    startActivity(intent);

                } else {
                    AndroidUtils.showToast(getApplicationContext(), "Error");

                }
            }
        });


    }

}