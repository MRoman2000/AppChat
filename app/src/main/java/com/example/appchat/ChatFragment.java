package com.example.appchat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appchat.adapter.ChatRecentRecyclerAdapter;
import com.example.appchat.adapter.SearchUserRecyclerAdapter;
import com.example.appchat.databinding.FragmentChatBinding;
import com.example.appchat.model.ChatRoomModel;
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    ChatRecentRecyclerAdapter adapter;

    public void chatFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_chat_view);
        setupSearchRecyclerView();
        return view;
    }

    private void setupSearchRecyclerView() {

        Query query = FirebaseUtil.allChatConnections()
                .whereArrayContains("userid", FirebaseUtil.currentUser())
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>().setQuery(query, ChatRoomModel.class).build();
        adapter = new ChatRecentRecyclerAdapter(options, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}