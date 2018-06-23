package com.indian.states.capitals.indianstates;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;

public class DetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private ProgressBar mProgressBar;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton mActionButton;

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youTubePlayerView;

    CircleIndicator circleIndicator;
    private AutoScrollViewPager viewPager;
    private PagerAdapter adapter;

    private ArrayList<String> images;
    private ArrayList<String> imageDetails;

    private DatabaseReference databaseReference;

    private TextView capital, area, population, literacyRate, literacyRateMale, literacyRateFemale, sexRatio;
    private ImageView appbarImageView;
    private ExpandableTextView historyTextView, languages, regionalDance;

    private String youTubeVideoLink;

    private StateDetails stateDetails;
    private String stateName;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);


        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mRef.keepSynced(true);

        viewPager = findViewById(R.id.view_pager);
        circleIndicator = findViewById(R.id.indicator);
        mProgressBar = findViewById(R.id.progress_bar);

        viewPager.startAutoScroll();
        viewPager.setInterval(5000);
        viewPager.setCycle(true);
        viewPager.setStopScrollWhenTouch(true);

        youTubePlayerView = findViewById(R.id.youtube_player_view);


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int margin = 10;
                final int fragmentOffset = view.getScrollX() % view.getWidth();

                if (fragmentOffset > margin && fragmentOffset < view.getWidth() - margin) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        youTubePlayerView.initialize(BuildConfig.ApiKey, this);

        population = findViewById(R.id.population);
        capital = findViewById(R.id.capital);
        area = findViewById(R.id.area);


        coordinatorLayout = findViewById(R.id.detail_container);
        appbarImageView = findViewById(R.id.app_bar_image);
        historyTextView = findViewById(R.id.expand_text_view);
        languages = findViewById(R.id.languages_spoken);
        regionalDance = findViewById(R.id.regional_dance);
        literacyRate = findViewById(R.id.literacy_rate);
        literacyRateMale = findViewById(R.id.literacy_rate_male);
        literacyRateFemale = findViewById(R.id.literacy_rate_female);
        sexRatio = findViewById(R.id.sex_ratio);


        Intent intent = getIntent();
        if (intent.hasExtra("State")) {
            stateName = intent.getStringExtra("State");
            collapsingToolbarLayout.setTitle(intent.getStringExtra("State"));
            databaseReference = FirebaseDatabase.getInstance().getReference().child("States").child(intent.getStringExtra("State").trim());
        }


        //Connectivity Manager
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            mProgressBar.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.detail_activity), "No Internet Connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        mActionButton = findViewById(R.id.detail_action);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> bookmark = new HashMap<String, String>();
                bookmark.put(stateName, "".trim());
                mRef.child("Bookmarks").child(stateName).setValue(bookmark).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DetailActivity.this, stateName+" added to the Bookmarks", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

   /* public void addDetailsToBookmark() {

        HashMap<String, String> details = new HashMap<String, String>();

        details.put("area", stateDetails.getArea());
        details.put("capital", stateDetails.getCapital());
        details.put("history", stateDetails.getHistory());
        details.put("languages", stateDetails.getLanguages());
        details.put("literacyRate", String.valueOf(stateDetails.getLiteracyRate()));
        details.put("literacyRateFemale", String.valueOf(stateDetails.getLiteracyRateFemale()));
        details.put("literacyRateMale", String.valueOf(stateDetails.getLiteracyRateMale()));
        details.put("population", String.valueOf(stateDetails.getPopulation()));
        details.put("regionalDances", stateDetails.getRegionalDance());
        details.put("sexRatio", String.valueOf(stateDetails.getSexRatio()));
        mRef.child("Bookmarks").child(stateName).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DetailActivity.this, "Bookmark Added", Toast.LENGTH_SHORT).show();
                HashMap<String, String> Imagedetails = new HashMap<String, String>();
                HashMap<String, String> images = new HashMap<String, String>();
                int i = 0;
                ArrayList<String> imageDetails = stateDetails.getImageDetails();
                for (i = 0; i < imageDetails.size(); i++) {
                    Imagedetails.put(" " + i, imageDetails.get(i));

                }
                mRef.child("Bookmarks").child(stateName).child("imageDetails").setValue(Imagedetails);
                i = 0;
                ArrayList<String> img = stateDetails.getImages();
                for (i = 0; i < img.size(); i++) {
                    Imagedetails.put(" " + i, img.get(i));
                }
                mRef.child("Bookmarks").child(stateName).child("images").setValue(img);
                Toast.makeText(DetailActivity.this, "Bookmark Added", Toast.LENGTH_SHORT).show();
            }

        });

    }*/

    private void readDataFromDatabase(final StatesCallback statesCallback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stateDetails = dataSnapshot.getValue(StateDetails.class);
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
                if (!b) {
                    Log.i("video link", youTubeVideoLink);
                    youTubePlayer.cueVideo(youTubeVideoLink);
                }
                mProgressBar.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);


            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(DetailActivity.this, RECOVERY_DIALOG_REQUEST);
        } else {
            String errorMessage = getString(R.string.youTubeErrorMessage) + youTubeInitializationResult;
            Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }

    }
}
