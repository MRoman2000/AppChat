package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appchat.adapter.SearchUserRecyclerAdapter;
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseException;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {


    EditText searchText;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);


        searchText = findViewById(R.id.edit_text_username);
        searchButton = findViewById(R.id.btnSearch);
        backButton = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.search_user_recycler_view);


        backButton.setOnClickListener(v -> {

            onBackPressed();
        });


        searchText.requestFocus();

        searchButton.setOnClickListener(v -> {
            String username = searchText.getText().toString();
            if (username.isEmpty() || username.length() < 3) {
                searchText.setError("No válido");
                return;
            }
            setupSearchRecyclerView(username);
        });
    }

    private void setupSearchRecyclerView(String username) {

        Query query = FirebaseUtil.allusers()
                .orderBy("username")
                .startAt(username)
                .endAt(username);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();
        adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}