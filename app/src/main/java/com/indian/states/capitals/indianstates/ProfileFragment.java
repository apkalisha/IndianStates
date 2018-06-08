package com.indian.states.capitals.indianstates;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {
    View profileFragement;
    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();

        return profileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileFragement = inflater.inflate(R.layout.fragment_profile,container,false);
        return profileFragement;
    }
}
