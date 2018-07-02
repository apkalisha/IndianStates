package com.indian.states.capitals.indianstates;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


class SectionPagerAdapter extends FragmentStatePagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                return new StatesFragment();
            case 1:

                return new UnionFragment();

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "States";
            case 1:
                return "Union Territories";
            default:
                return null;
        }
    }
}
