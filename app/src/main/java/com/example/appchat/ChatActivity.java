package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appchat.model.UserModel;
import com.example.appchat.utils.AndroidUtils;

import org.w3c.dom.Text;

public class ChatActivity extends AppCompatActivity {
    UserModel otros;
    EditText message;
    ImageButton send;
    TextView username;
    ImageButton btnBack;
    RecyclerView recycler;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otros = AndroidUtils.getUserModel(getIntent());
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        message = (EditText) findViewById(R.id.message_edit_text);
        send = (ImageButton) findViewById(R.id.btn_send);
        username = (TextView) findViewById(R.id.username_text);
        recycler = findViewById(R.id.recycler_view);
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        username.setText(otros.getUsername());

    }
}