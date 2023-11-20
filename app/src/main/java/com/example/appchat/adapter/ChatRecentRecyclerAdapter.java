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

        FirebaseUtil.getOtrosUser(model.getUserIds()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUser());


                UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                FirebaseUtil.getOtherProfileStorage(otherUserModel.getUserID()).getDownloadUrl()
                        .addOnCompleteListener(t -> {
                            if (t.isSuccessful()) {
                                Uri uri = t.getResult();
                                AndroidUtils.setProfilePic(context, uri, holder.imageView);
                            }
                        });

                holder.usernameText.setText(otherUserModel.getUsername());
                if (lastMessageSentByMe)
                    holder.lastMessageText.setText("Tu : " + model.getLastMessage());
                else
                    holder.lastMessageText.setText(model.getLastMessage());
                holder.LastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                holder.itemView.setOnClickListener(v -> {
                    //navigate to chat activity
                    Intent intent = new Intent(context, ChatActivity.class);
                    AndroidUtils.passUserModel(intent, otherUserModel);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
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
        ImageView imageView;

        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            lastMessageText = itemView.findViewById(R.id.last_message);
            LastMessageTime = itemView.findViewById(R.id.last_time_message);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }
}
