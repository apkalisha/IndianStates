package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements StateAdapter.StateAdapterOnClickHandler {

    private View homeFragment;
    private RecyclerView recyclerView;

    private StateAdapter stateAdapter;

    private List<String> states;
    private MaterialSearchView materialSearchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeFragment = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = homeFragment.findViewById(R.id.recyclerview_states);
        states = Arrays.asList(getResources().getStringArray(R.array.india_states));

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        materialSearchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        stateAdapter = new StateAdapter(this);
        stateAdapter.setStateNames(states);
        recyclerView.setAdapter(stateAdapter);

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

        return homeFragment;
    }



    @Override
    public void onItemClick(String state) {


            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("State", state);
            startActivity(intent);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.main,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(menuItem);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
