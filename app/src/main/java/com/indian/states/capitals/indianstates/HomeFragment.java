package com.indian.states.capitals.indianstates;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements StateAdapter.StateAdapterOnClickHandler{

    private View homeFragment;

    private Context mContext;

    private RecyclerView recyclerView;

    private StateAdapter stateAdapter;

    private List<String> states;
    private List<String> unions;
    private List<String> searchItems;
    private MaterialSearchView materialSearchView;
    private Spinner spinner;

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
        homeFragment = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = homeFragment.findViewById(R.id.recyclerview_states);
        states = Arrays.asList(getResources().getStringArray(R.array.india_states));
        unions = Arrays.asList(getResources().getStringArray(R.array.union_territories));

        materialSearchView =  getActivity().findViewById(R.id.search_view);
        spinner = homeFragment.findViewById(R.id.spinner);
        String[] items = {"States","Union Territories"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        stateAdapter.setStateNames(states);
                        searchItems = states;
                        break;
                    case 1:
                        stateAdapter.setStateNames(unions);
                        searchItems = unions;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mContext = getActivity();
        stateAdapter = new StateAdapter(this,0);
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
                    for (String item : searchItems) {
                        if (item.toLowerCase().contains(newText.toLowerCase())) {
                            listFound.add(item);
                        }
                    }
                    stateAdapter.setStateNames(listFound);
                    recyclerView.setAdapter(stateAdapter);
                } else {
                    stateAdapter.setStateNames(searchItems);
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
