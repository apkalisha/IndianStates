package com.indian.states.capitals.indianstates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BookmarkFragment extends Fragment {

    View bookmarkFragment;

    public static BookmarkFragment newInstance() {

        return new BookmarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bookmarkFragment = inflater.inflate(R.layout.fragment_bookmark,container,false);
        return bookmarkFragment;
    }
}
