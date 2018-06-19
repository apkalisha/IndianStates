package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class DetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private ProgressBar mProgressBar;
    private CoordinatorLayout coordinatorLayout;

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youTubePlayerView;

    CircleIndicator circleIndicator;
    private AutoScrollViewPager viewPager;
    private PagerAdapter adapter;

    private ArrayList<String> images;
    private ArrayList<String> imageDetails;

    private DatabaseReference databaseReference;

    private TextView capital, area, population,literacyRate,literacyRateMale,literacyRateFemale,sexRatio;
    private ImageView appbarImageView;
    private ExpandableTextView historyTextView, languages, regionalDance;

    private String youTubeVideoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);


        Toolbar toolbar=findViewById(R.id.detail_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        viewPager = findViewById(R.id.view_pager);
        circleIndicator = findViewById(R.id.indicator);
        mProgressBar = findViewById(R.id.progress_bar);

        viewPager.startAutoScroll();
        viewPager.setInterval(5000);
        viewPager.setCycle(true);
        viewPager.setStopScrollWhenTouch(true);

        youTubePlayerView =  findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(BuildConfig.ApiKey, this);

        population = findViewById(R.id.population);
        capital = findViewById(R.id.capital);
        area = findViewById(R.id.area);

        coordinatorLayout = findViewById(R.id.detail_container);
        appbarImageView = findViewById(R.id.app_bar_image);
        historyTextView = findViewById(R.id.expand_text_view);
        languages=findViewById(R.id.languages_spoken);
        regionalDance=findViewById(R.id.regional_dance);
        literacyRate = findViewById(R.id.literacy_rate);
        literacyRateMale = findViewById(R.id.literacy_rate_male);
        literacyRateFemale = findViewById(R.id.literacy_rate_female);
        sexRatio = findViewById(R.id.sex_ratio);

        Intent intent = getIntent();
        if(intent.hasExtra("State")) {
            collapsingToolbarLayout.setTitle(intent.getStringExtra("State"));
            databaseReference = FirebaseDatabase.getInstance().getReference().child("States").child(intent.getStringExtra("State").trim());
        }
    }

    private void readDataFromDatabase(final StatesCallback statesCallback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StateDetails stateDetails = dataSnapshot.getValue(StateDetails.class);
                statesCallback.onCallback(stateDetails);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, final boolean b) {

        readDataFromDatabase(new StatesCallback() {
            @Override
            public void onCallback(StateDetails stateDetails) {
                capital.setText(stateDetails.getCapital());
                area.setText(stateDetails.getArea());
                population.setText(stateDetails.getPopulation());
                historyTextView.setText(stateDetails.getHistory());
                languages.setText(stateDetails.getLanguages());
                regionalDance.setText(stateDetails.getRegionalDance());
                literacyRate.setText(String.valueOf(stateDetails.getLiteracyRate()));
                literacyRateMale.setText(String.valueOf(stateDetails.getLiteracyRateMale()));
                literacyRateFemale.setText(String.valueOf(stateDetails.getLiteracyRateFemale()));
                sexRatio.setText(String.valueOf(stateDetails.getSexRatio()));
                images = stateDetails.getImages();
                imageDetails = stateDetails.getImageDetails();
                youTubeVideoLink = stateDetails.getYouTubeVideoLink();

                Picasso.get().load(images.get(0)).into(appbarImageView);
                adapter = new CustomAdapter(DetailActivity.this, images, imageDetails);
                viewPager.setAdapter(adapter);
                circleIndicator.setViewPager(viewPager);
                adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                if(!b) {
                    Log.i("video link",youTubeVideoLink);
                    youTubePlayer.cueVideo(youTubeVideoLink);
                }
                mProgressBar.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(DetailActivity.this,RECOVERY_DIALOG_REQUEST);
        } else {
            String errorMessage = getString(R.string.youTubeErrorMessage)+youTubeInitializationResult;
            Toast.makeText(DetailActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
        }

    }
}
