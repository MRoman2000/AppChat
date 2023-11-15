package com.example.appchat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.ChatActivity;
import com.example.appchat.R;
import com.example.appchat.model.ChatMessageModel;
import com.example.appchat.utils.AndroidUtils;
import com.example.appchat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatMessageModelViewHolder> {

    Context context;

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ChatMessageModelViewHolder holder, int position, @NonNull ChatMessageModel model) {

        if (model.getUsernameId().equals(FirebaseUtil.currentUser())) {
            holder.leftchatLayout.setVisibility(View.GONE);
            holder.rightchatLayout.setVisibility(View.VISIBLE);
            holder.chatright.setText(model.getMessage());
        } else {
            holder.rightchatLayout.setVisibility(View.GONE);
            holder.leftchatLayout.setVisibility(View.VISIBLE);
            holder.chatleft.setText(model.getMessage());

        }


    }

    @NonNull
    @Override
    public ChatMessageModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler, parent, false);

        return new ChatMessageModelViewHolder(view);
    }

    class ChatMessageModelViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftchatLayout, rightchatLayout;
        TextView chatleft, chatright;

        public ChatMessageModelViewHolder(@NonNull View itemView) {
            super(itemView);

            leftchatLayout = itemView.findViewById(R.id.left_message);
            rightchatLayout = itemView.findViewById(R.id.right_message);
            chatleft = itemView.findViewById(R.id.left_chat);
            chatright = itemView.findViewById(R.id.right_chat);
        }
    }
}
