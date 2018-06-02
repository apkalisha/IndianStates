package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StateAdapter.StateAdapterOnClickHandler{

    private RecyclerView recyclerView;

    private StateAdapter stateAdapter;

    private List<String> states;

    private MaterialSearchView materialSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_states);
        states = Arrays.asList(getResources().getStringArray(R.array.india_states));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        stateAdapter = new StateAdapter(this);
        stateAdapter.setStateNames(states);
        recyclerView.setAdapter(stateAdapter);


        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()) {
                    List<String> listFound = new ArrayList<>();
                    for(String item : states){
                        if(item.toLowerCase().contains(newText.toLowerCase())){
                            listFound.add(item);
                        }
                    }
                    stateAdapter.setStateNames(listFound);
                    recyclerView.setAdapter(stateAdapter);
                } else {
                    stateAdapter.setStateNames(states);
                    recyclerView.setAdapter(stateAdapter);
                }
                return true;
            }
        });
    }

    @Override
    public void onItemClick(String state) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("State",state);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(menuItem);
        return true;
    }
}

