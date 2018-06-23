package com.indian.states.capitals.indianstates;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;


public class IndianStates extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
