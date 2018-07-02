package com.indian.states.capitals.indianstates;


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
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements StateAdapter.StateAdapterOnClickHandler{

    View homeFragment;
    private RecyclerView recyclerView;

    private StateAdapter stateAdapter;

    private List<String> states;
    private MaterialSearchView materialSearchView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        homeFragment = inflater.inflate(R.layout.fragment_home,container,false);
        ViewPager mViewPager = homeFragment.findViewById(R.id.main_tabPager);
        TabLayout mTabLayout = homeFragment.findViewById(R.id.main_tabs);
        SectionPagerAdapter mSectionPagerAdapter = new SectionPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        states = Arrays.asList(getResources().getStringArray(R.array.india_states));
        recyclerView = homeFragment.findViewById(R.id.recyclerview_states);
        materialSearchView =  getActivity().findViewById(R.id.search_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        stateAdapter = new StateAdapter(this,0);
        stateAdapter.setStateNames(states);
       // recyclerView.setAdapter(stateAdapter);

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
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(stateAdapter);
                } else {
                    stateAdapter.setStateNames(states);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(stateAdapter);
                }
                return true;
            }
        });

        return homeFragment;
    }

    @Override
    public void onItemClick(String state) {
        Toast.makeText(getContext(),"CLICKED",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnFavClick(String state, Integer pos) {

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(menuItem);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
