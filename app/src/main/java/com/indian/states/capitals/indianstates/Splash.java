package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        ImageView imageView = (ImageView) findViewById(R.id.splash_img);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        imageView.startAnimation(animation);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            LogoLauncher2 logoLauncher2 = new LogoLauncher2();
            logoLauncher2.start();
        }
        else {
            LogoLauncher logoLauncher = new LogoLauncher();
            logoLauncher.start();
        }

    }


    class LogoLauncher extends Thread {
        public void run() {

            try {
                sleep(4000);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Splash.this.finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
    }

        class LogoLauncher2 extends Thread {
            public void run() {

                try {
                    sleep(4000);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    Splash.this.finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        }


    }

