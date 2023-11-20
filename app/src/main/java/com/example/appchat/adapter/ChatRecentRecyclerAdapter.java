package com.example.appchat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.ChatActivity;
import com.example.appchat.R;
import com.example.appchat.model.ChatRoomModel;
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.AndroidUtils;
import com.example.appchat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecentRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, ChatRecentRecyclerAdapter.ChatRoomModelViewHolder> {

    Context context;

    public ChatRecentRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {


        FirebaseUtil.getChatRoomReference(model.getChatroomId()).get().addOnCompleteListener(task ->  {

            if(task.isSuccessful()) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                holder.usernameText.setText(userModel.getUsername());
                holder.lastMessageText.setText(model.getLastMessage());
                holder.lastMessageText.setText(model.getLastMessagetimestamp().toString());
            }
        });
    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_recycler_chat, parent, false);

        return new ChatRoomModelViewHolder(view);
    }

    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText;
        TextView lastMessageText;
        TextView LastMessageTime;

        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            lastMessageText = itemView.findViewById(R.id.last_message);
            LastMessageTime = itemView.findViewById(R.id.last_time_message);


        }
    }
}
