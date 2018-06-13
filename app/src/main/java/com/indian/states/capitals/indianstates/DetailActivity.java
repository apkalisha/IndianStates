package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class DetailActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private CoordinatorLayout coordinatorLayout;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    CircleIndicator circleIndicator;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    private ArrayList<String> images;
    private ArrayList<String> imageDetails;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView capital, area, population,languages,literacyRate;
    private ImageView appbarImageView;
    private ExpandableTextView historyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        CollapsingToolbarLayout toolbar = findViewById(R.id.collapsing_toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        viewPager = findViewById(R.id.view_pager);
        circleIndicator = findViewById(R.id.indicator);
        mProgressBar = findViewById(R.id.progress_bar);

        firebaseDatabase = FirebaseDatabase.getInstance();

        population = findViewById(R.id.population);
        capital = findViewById(R.id.capital);
        area = findViewById(R.id.area);

        coordinatorLayout = findViewById(R.id.detail_container);
        appbarImageView = findViewById(R.id.app_bar_image);
        historyTextView = findViewById(R.id.expand_text_view);
        literacyRate = findViewById(R.id.literacy_rate);
        languages = findViewById(R.id.languages);

        Intent intent = getIntent();
        if(intent.hasExtra("State")) {
            toolbar.setTitle(intent.getStringExtra("State"));
            databaseReference = firebaseDatabase.getReference().child("States").child(intent.getStringExtra("State").trim());
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressBar.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                StateDetails stateDetails = dataSnapshot.getValue(StateDetails.class);
                capital.setText(stateDetails.getCapital());
                area.setText(stateDetails.getArea());
                population.setText(stateDetails.getPopulation());
                historyTextView.setText(stateDetails.getHistory());
                languages.setText(stateDetails.getLanguages());
                literacyRate.setText(String.valueOf(stateDetails.getLiteracyRate()));
                images = stateDetails.getImages();
                imageDetails = stateDetails.getImageDetails();
                Picasso.get().load(images.get(0)).into(appbarImageView);
                adapter = new CustomAdapter(DetailActivity.this,images,imageDetails);
                viewPager.setAdapter(adapter);
                circleIndicator.setViewPager(viewPager);
                adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == 15) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },DELAY_MS,PERIOD_MS);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
