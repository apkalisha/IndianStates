package com.indian.states.capitals.indianstates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment implements StateAdapter.StateAdapterOnClickHandler{

    View bookmarkFragment;

    private RecyclerView recyclerView;
    private StateAdapter stateAdapter;

    private Context mContext;

    private ArrayList<String> states = new ArrayList<>();

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;


    public static BookmarkFragment newInstance() {

        return new BookmarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bookmarkFragment = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = bookmarkFragment.findViewById(R.id.recyclerview_states);

        mAuth = FirebaseAuth.getInstance();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabase.keepSynced(true);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mContext = getActivity();
        stateAdapter = new StateAdapter(this,1);
        loadState();


        return bookmarkFragment;
    }

    public void loadState() {
        mDatabase.child("Bookmarks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = String.valueOf(ds.getKey());
                    states.add(name);

                }
                stateAdapter.setStateNames(states);
                recyclerView.setAdapter(stateAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void OnFavClick(String state, Integer position) {
        Toast.makeText(this.getActivity(), "Removed " + state + " from Bookmarks", Toast.LENGTH_SHORT).show();
        mDatabase.child("Bookmarks").child(state).removeValue();
        stateAdapter = new StateAdapter(this,1);
        states.clear();
        stateAdapter.notifyItemRemoved(position);
        stateAdapter.notifyItemRangeChanged(position, 1);


    }

    @Override
    public void onItemClick(String state) {


        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("State", state);
        startActivity(intent);

    }



}
