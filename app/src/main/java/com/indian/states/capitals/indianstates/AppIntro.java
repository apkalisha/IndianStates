package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class AppIntro extends com.github.paolorotolo.appintro.AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(SampleSlide.newInstance(R.layout.intro_slide1));
        addSlide(SampleSlide.newInstance(R.layout.intro_slide2));
        addSlide(SampleSlide.newInstance(R.layout.intro_slide3));
        addSlide(SampleSlide.newInstance(R.layout.intro_slide4));

        //setSeparatorColor(Color.parseColor("#2196F3"));

        showStatusBar(false);

        showSkipButton(true);

        setFadeAnimation();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

}
