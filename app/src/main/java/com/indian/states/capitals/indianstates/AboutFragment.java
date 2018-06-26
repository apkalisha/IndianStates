package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AboutFragment extends Fragment {
    View aboutFragment;
    TextView tv1, tv2, tv3, tv4;
    private String profile;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        aboutFragment = inflater.inflate(R.layout.fragment_about, container, false);
        tv1 = aboutFragment.findViewById(R.id.about_tv1);
        tv2 = aboutFragment.findViewById(R.id.about_tv2);
        tv3 = aboutFragment.findViewById(R.id.about_tv3);
        tv4 = aboutFragment.findViewById(R.id.about_tv4);

        tv1.setText("Alisha Saluja");
        tv2.setText("Ayush Singh");
        tv3.setText("Prakash Kumar");
        tv4.setText("Kamesh Kumar Singh");
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = "alisha-saluja-481209163";
                openLinkedn(profile);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = "ayush-singh-644805141";
                openLinkedn(profile);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = "prakashk141";
                openLinkedn(profile);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile = "kameshk61";
                openLinkedn(profile);
            }
        });
        return aboutFragment;

    }

    public void openLinkedn(String profileId) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + profileId));
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + profileId));
        }
        startActivity(intent);
    }


}
