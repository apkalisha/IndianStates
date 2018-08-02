package com.indian.states.capitals.indianstates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private  Fragment selectedFragment = null;
    private MaterialSearchView searchView;
    private NavigationView navigationView;
    private static int index = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_nav_24dp);

        searchView = findViewById(R.id.search_view);

        mAuth = FirebaseAuth.getInstance();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, AppIntro.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

      final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
      BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        navigationView.getMenu().getItem(0).setChecked(true);
                       selectedFragment = HomeFragment.newInstance();
                        setTitle("Indian States");
                        break;
                    case R.id.navigation_bookmarks:
                        navigationView.getMenu().getItem(index).setChecked(false);
                        selectedFragment = BookmarkFragment.newInstance();
                        setTitle(item.getTitle());
                        break;
                    case R.id.navigation_profile:
                        navigationView.getMenu().getItem(index).setChecked(false);
                        selectedFragment = ProfileFragment.newInstance();
                        setTitle(item.getTitle());
                        break;
                    case R.id.navigation_quiz:
                        navigationView.getMenu().getItem(index).setChecked(false);
                        selectedFragment = QuizCategoryFragment.newInstance();
                        setTitle(item.getTitle());
                        break;
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, selectedFragment);
                fragmentTransaction.commit();
                return true;
            }
        });
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                //bottomNavigationView.setVisibility(View.VISIBLE);
                                index = 0;
                                setDefaultFragment();
                                setTitle("Indian States and UTs");
                                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                                break;
                            case R.id.nav_about_us:
                                //bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId()).setCheckable(false);
                                //bottomNavigationView.setVisibility(View.INVISIBLE);
                                index = 1;
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, AboutFragment.newInstance());
                                setTitle(menuItem.getTitle());
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_share:
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT,
                                        "Hey check out my app at playstore: https://play.google.com/store/apps/details?id=com.indian.states.capitals.indianstates");
                                sendIntent.setType("text/plain");
                                startActivity(sendIntent);
                                Toast.makeText(MainActivity.this,"Share",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_contact_use:
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto","dzoneassociation@gmail.com", null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                break;
                            case R.id.nav_exit:
                                finish();
                                break;

                        }
                        return true;
                    }
                });



        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });

        //setting home fragment
       setDefaultFragment();


        //Connectivity Manager
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.container), "No Internet Connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void setDefaultFragment() {
        navigationView.getMenu().getItem(0).setChecked(true);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(searchView.isSearchOpen()) {
                searchView.closeSearch();
                return;
            }
            else if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Snackbar.make(findViewById(R.id.snackbar_container), "Press back again to exit", Snackbar.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(mainIntent);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
