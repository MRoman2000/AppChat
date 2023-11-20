package com.example.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appchat.adapter.ChatRecyclerAdapter;
import com.example.appchat.adapter.SearchUserRecyclerAdapter;
import com.example.appchat.model.ChatMessageModel;
import com.example.appchat.model.ChatRoomModel;
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.AndroidUtils;
import com.example.appchat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    UserModel otros;
    String chatroomId;
    ChatRoomModel chatRoomModel;
    ChatRecyclerAdapter adapterchat;
    EditText messageEdit;
    ImageButton send;
    TextView username;
    ImageButton btnBack;
    RecyclerView recyclerView;
    ImageView imageView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otros = AndroidUtils.getUserModel(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUser(), otros.getUserID());
        btnBack = (ImageButton) findViewById(R.id.btnAtras);
        messageEdit = (EditText) findViewById(R.id.message_edit_text);
        send = (ImageButton) findViewById(R.id.btn_send);
        username = (TextView) findViewById(R.id.username_text);
        recyclerView = findViewById(R.id.recycler_view);
        imageView = findViewById(R.id.image_view);


        FirebaseUtil.getOtherProfileStorage(otros.getUserID()).getDownloadUrl().addOnCompleteListener(t -> {
            if (t.isSuccessful()) {
                Uri uri = t.getResult();
                AndroidUtils.setProfilePic(this, uri, imageView);
            }
        });
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        username.setText(otros.getUsername());

        send.setOnClickListener((v -> {
            String message = messageEdit.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessage(message);
        }));

        getOnCreateChatroomModel();
        getChatRecyclerView();
    }

    private void getChatRecyclerView() {
        Query query = FirebaseUtil.getChatMessageReference(chatroomId).orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>().setQuery(query, ChatMessageModel.class).build();
        adapterchat = new ChatRecyclerAdapter(options, getApplicationContext());
        recyclerView.setAdapter(adapterchat);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterchat);
        adapterchat.startListening();
        adapterchat.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });


    }

    private void sendMessage(String message) {

        chatRoomModel.setLastMessagetimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(FirebaseUtil.currentUser());
        chatRoomModel.setLastMessage(message);
        FirebaseUtil.getChatRoomReference(chatroomId).set(chatRoomModel);
        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtil.currentUser(), Timestamp.now());
        FirebaseUtil.getChatMessageReference(chatroomId).add((chatMessageModel)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    messageEdit.setText("");
                }
            }
        });


    }

    private void getOnCreateChatroomModel() {

        FirebaseUtil.getChatRoomReference(chatroomId).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if (chatRoomModel == null) {
                    chatRoomModel = new ChatRoomModel(chatroomId,
                            Arrays.asList(FirebaseUtil.currentUser(), otros.getUserID()), Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatRoomReference(chatroomId).set(chatRoomModel);
                }

            }

        });

    }
}