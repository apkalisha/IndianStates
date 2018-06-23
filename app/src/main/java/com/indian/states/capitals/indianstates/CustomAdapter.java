package com.indian.states.capitals.indianstates;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<String> imagesArray;
    private ArrayList<String> namesArray;

    public CustomAdapter(Activity activity, ArrayList<String> imagesArray, ArrayList<String> namesArray) {

        this.activity = activity;
        this.imagesArray = imagesArray;
        this.namesArray = namesArray;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_viewpager,container,false);
        ImageView imageView = view.findViewById(R.id.image_viewpager);
        TextView textView = view.findViewById(R.id.tv_image_name);
        Picasso.get()
                .load(imagesArray.get(position+1))
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(imageView);
        textView.setText(namesArray.get(position));
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return imagesArray.size()-1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
