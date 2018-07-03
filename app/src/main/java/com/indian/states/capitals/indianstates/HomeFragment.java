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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements StateAdapter.StateAdapterOnClickHandler{

    private Context mContext;

    private RecyclerView recyclerView;

    private StateAdapter stateAdapter;

    private List<String> states;
    private List<String> statesArea;
    private List<String> statesPopulation;
    private List<String> statesLiteracyRate;
    private List<String> statesSexRatio;
    private List<String> unions;
    private List<String> unionsArea;
    private List<String> unionsPopulation;
    private List<String> unionsLiteracyRate;
    private List<String> unionsSexRatio;
    private List<String> searchItems;
    private MaterialSearchView materialSearchView;
    private String selected;

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
        View homeFragment = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = homeFragment.findViewById(R.id.recyclerview_states);

        states = Arrays.asList(getResources().getStringArray(R.array.india_states));
        statesArea = Arrays.asList(getResources().getStringArray(R.array.states_area));
        statesPopulation = Arrays.asList(getResources().getStringArray(R.array.states_population));
        statesLiteracyRate = Arrays.asList(getResources().getStringArray(R.array.states_literacy_rate));
        statesSexRatio = Arrays.asList(getResources().getStringArray(R.array.states_sex_ratio));

        unions = Arrays.asList(getResources().getStringArray(R.array.union_territories));
        unionsArea = Arrays.asList(getResources().getStringArray(R.array.ut_area));
        unionsPopulation = Arrays.asList(getResources().getStringArray(R.array.ut_population));
        unionsLiteracyRate = Arrays.asList(getResources().getStringArray(R.array.ut_literacy_rate));
        unionsSexRatio = Arrays.asList(getResources().getStringArray(R.array.ut_sex_ratio));

        materialSearchView =  getActivity().findViewById(R.id.search_view);
        Spinner spinner = homeFragment.findViewById(R.id.spinner);
        final Spinner sortSpinner = homeFragment.findViewById(R.id.sort_spinner);
        LinearLayout sortSpinnerLayout = homeFragment.findViewById(R.id.sort_spinner_container);

        spinner.setVisibility(View.VISIBLE);
        sortSpinnerLayout.setVisibility(View.VISIBLE);

        String[] items = {"States","Union Territories"};
        String[] sortOptions = {"Name","Area","Population","Literacy Rate","Sex Ratio"};

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
                        selected = "States";
                        sortSpinner.setSelection(0);
                        break;
                    case 1:
                        stateAdapter.setStateNames(unions);
                        searchItems = unions;
                        selected = "Union Territories";
                        sortSpinner.setSelection(0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if(selected.equals("States"))
                            stateAdapter.setStateNames(states);
                        else
                            stateAdapter.setStateNames(unions);
                        break;
                    case 1:
                        if(selected.equals("States"))
                            stateAdapter.setStateNames(statesArea);
                        else
                            stateAdapter.setStateNames(unionsArea);
                        break;
                    case 2:
                        if(selected.equals("States"))
                            stateAdapter.setStateNames(statesPopulation);
                        else
                            stateAdapter.setStateNames(unionsPopulation);
                        break;
                    case 3:
                        if(selected.equals("States"))
                            stateAdapter.setStateNames(statesLiteracyRate);
                        else
                            stateAdapter.setStateNames(unionsLiteracyRate);
                        break;
                    case 4:
                        if(selected.equals("States"))
                            stateAdapter.setStateNames(statesSexRatio);
                        else
                            stateAdapter.setStateNames(unionsSexRatio);
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
