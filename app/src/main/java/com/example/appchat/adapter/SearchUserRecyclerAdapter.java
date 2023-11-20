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
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.AndroidUtils;
import com.example.appchat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.SearchUserRecyclerHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull SearchUserRecyclerHolder holder, int position, @NonNull UserModel model) {

        holder.usernameText.setText(model.getUsername());
        holder.telefonoText.setText(model.getTelefono());
        if (model.getUserID().equals(FirebaseUtil.currentUser())) {
            holder.usernameText.setText(model.getUsername() + (" (Yo) "));
        }
        else  {
            FirebaseUtil.getOtherProfileStorage(model.getUserID()).getDownloadUrl().addOnCompleteListener(t -> {
                if (t.isSuccessful()) {
                    Uri uri = t.getResult();
                    AndroidUtils.setProfilePic(context, uri, holder.imageView);
                }
            });


            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                AndroidUtils.passUserModel(intent, model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }


    }

    @NonNull
    @Override
    public SearchUserRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler, parent, false);

        return new SearchUserRecyclerHolder(view);
    }

    class SearchUserRecyclerHolder extends RecyclerView.ViewHolder {

        TextView usernameText;
        TextView telefonoText;
        ImageView imageView;

        public SearchUserRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            telefonoText = itemView.findViewById(R.id.telefono_text);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
