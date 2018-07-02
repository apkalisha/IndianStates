package com.indian.states.capitals.indianstates;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    View homeFragment;

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
        return homeFragment;
    }
}
