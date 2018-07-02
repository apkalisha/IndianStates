package com.indian.states.capitals.indianstates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class StatesFragment extends Fragment implements StateAdapter.StateAdapterOnClickHandler {

    private View stateFragment;

    private Context mContext;

    private RecyclerView recyclerView;

    private StateAdapter stateAdapter;

    private List<String> states;
    private MaterialSearchView materialSearchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        stateFragment = inflater.inflate(R.layout.fragment_states, container, false);
        recyclerView = stateFragment.findViewById(R.id.recyclerview_states);
        states = Arrays.asList(getResources().getStringArray(R.array.india_states));

        materialSearchView =  getActivity().findViewById(R.id.search_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mContext = getActivity();
        stateAdapter = new StateAdapter(this,0);
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
                if (newText != null && !newText.isEmpty()) {
                    List<String> listFound = new ArrayList<>();
                    for (String item : states) {
                        if (item.toLowerCase().contains(newText.toLowerCase())) {
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

        return stateFragment;
    }


    @Override
    public void onItemClick(String state) {


        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("State", state);
        startActivity(intent);

    }

    @Override
    public void OnFavClick(String state, Integer p) {
        //nothing to do here
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(menuItem);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
